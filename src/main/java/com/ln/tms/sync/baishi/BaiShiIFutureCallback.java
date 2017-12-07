package com.ln.tms.sync.baishi;

import com.ln.tms.bean.BaiShiTrace;
import com.ln.tms.bean.BaiShiTrace.TraceLogsBean;
import com.ln.tms.bean.BaiShiTrace.TraceLogsBean.TracesBean;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;
import com.ln.tms.service.InfoService;
import com.ln.tms.sync.IFutureCallback;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * YuanTongIFutureCallback
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings({"SpellCheckingInspection", "Duplicates"})
public class BaiShiIFutureCallback implements IFutureCallback<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaiShiIFutureCallback.class);
    private static final String BAISHI = "12402";
    private static final String YQS = "已签收";

    private InfoService infoService;

    private CountDownLatch countDownLatch;

    public BaiShiIFutureCallback(InfoService infoService, CountDownLatch countDownLatch) {
        this.infoService = infoService;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onSuccess(String data) {
        if (StringUtils.isNotBlank(data)) {
            //获取物流流转信息
            BaiShiTrace baiShiTrace = JsonUtils.toObject(data, BaiShiTrace.class);
            List<TraceLogsBean> TLList = baiShiTrace.getTraceLogs();
            for (TraceLogsBean traceLogsBean : TLList) {
                List<TracesBean> tTArr = traceLogsBean.getTraces();
                if (tTArr == null) {
                    LOGGER.warn("百世汇通快递，单号:" + traceLogsBean.getMailNo() + "暂没有物流流转信息");
                } else {
                    //key=快递单号，value=轨迹信息
                    Map<String, List<Trace>> map = new HashMap<>();
                    Trace infoTrace;
                    for (TracesBean tracesBean : tTArr) {
                        String key = traceLogsBean.getMailNo();
                        if (map.containsKey(key)) {
                            List<Trace> traces = map.get(key);
                            infoTrace = new Trace();
                            //快递单号
                            infoTrace.setLogisticCode(key);
                            //站点操作时间
                            infoTrace.setAcceptTime(tracesBean.getAcceptTime());
                            //站点操作描述
                            infoTrace.setAcceptStation(tracesBean.getRemark().replace("\n", ""));
                            //根据站点操作描述推导出当前到达的地点
                            infoTrace.setAcceptLocation(tracesBean.getAcceptAddress());
                            //根据站点操作描述推导出当前状态描述
                            infoTrace.setAction(this.synAction(tracesBean.getScanType(), tracesBean.getRemark()));
                            traces.add(infoTrace);
                        } else {
                            List<Trace> infoTraces = new ArrayList<>();
                            infoTrace = new Trace();
                            infoTrace.setLogisticCode(key);
                            infoTrace.setAcceptTime(tracesBean.getAcceptTime());
                            infoTrace.setAcceptStation(tracesBean.getRemark().replace("\n", ""));
                            infoTrace.setAcceptLocation(tracesBean.getAcceptAddress());
                            infoTrace.setAction(this.synAction(tracesBean.getScanType(), tracesBean.getRemark()));
                            infoTraces.add(infoTrace);
                            map.put(key, infoTraces);
                        }
                    }
                    //处理map
                    List<Info> plist = new ArrayList<>();
                    for (Map.Entry<String, List<Trace>> stringListEntry : map.entrySet()) {
                        //快递单号
                        String logisticCode = stringListEntry.getKey();
                        //对应的快递跟踪轨迹信息
                        List<Trace> traces = map.get(logisticCode);
                        //先查后改
                        Info t = new Info();
                        t.setLogisticCode(logisticCode);
                        t.setShipperCode(BAISHI);
                        List<Info> infoList = infoService.queryByWhere(t);
                        if (null != infoList) { //快递信息存在则更新
                            for (Info info : infoList) {
                                //第一条轨迹信息获取揽件操作时间
                                info.setTookTime(DateUtils.stringToDate(traces.get(0).getAcceptTime()));
                                //最后一条轨迹信息操作时间
                                info.setLastTime(DateUtils.stringToDate(traces.get(traces.size() - 1).getAcceptTime()));
                                //根据跟踪信息站点操作描述判断快递当前状态
                                info.setState(StateType.describeOf(traces.get(traces.size() - 1).getAction()));
                                //遍历轨迹信息如果在到达派件城市设置派件城市
                                for (Trace trace : traces) {
                                    if (trace.getAction().equals(StateType.DPJ.getDescribe())) {
                                        info.setPjCity(trace.getAcceptLocation());
                                    }
                                }
                                //处理问题件
                                if (traceLogsBean.getProblems() != null) {
                                    info.setState(StateType.describeOf("问题件"));
                                }
                                //快递已签收状态
                                if (info.getState().code() == 3) {
                                    //收件人签收时间(来自跟踪信息)
                                    info.setSignTime(info.getLastTime());
                                    //设置签收人(根据站点操作描述获取)
                                    info.setSignUser(getSignUser(traces.get(traces.size() - 1).getAcceptStation()));
                                    //到达城市
                                    info.setLocation(info.getPjCity());
                                    //时效（实际耗时）第一条信息时间到签收时间差
                                    info.setAttritTime(info.getSignTime().getTime() - info.getTookTime().getTime());
                                    //超时天数实际到达时间减去计划到达时间
                                    if (info.getSignTime() == null && info.getFileSignTime() != null) {
                                        info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getFileSignTime()));
                                    } else if (info.getSignTime() != null && info.getFileSignTime() == null) {
                                        info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getSignTime()));
                                    } else if (info.getSignTime() != null && info.getFileSignTime() != null) {
                                        if (DateUtils.getDateHours(info.getSignTime(), info.getFileSignTime()) < 24) {
                                            info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getFileSignTime()));
                                        } else {
                                            info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getSignTime()));
                                        }
                                    }
                                    //送货天数（在途天数）签收时间减发货时间（20点前做发运的，都当日开始计算，之后+1）
                                    DateTime shipmentsTime = new DateTime(info.getShipmentsTime());
                                    if (shipmentsTime.getHourOfDay() < 20) {
                                        info.setAttritDay(DateUtils.getDateDifference(info.getShipmentsTime(), info.getSignTime()));
                                    }
                                    if (shipmentsTime.getHourOfDay() >= 20) {
                                        info.setAttritDay(DateUtils.getDateDifference(info.getShipmentsTime(), info.getSignTime()) + 1);
                                    }
                                } else { //其他状态
                                    info.setSignTime(null);
                                    info.setSignUser(null);
                                    info.setLocation(traces.get(traces.size() - 1).getAcceptLocation());
                                    info.setAttritTime(null);
                                    info.setOverDay(null);
                                    info.setAttritDay(null);
                                    //在快递柜自提柜驿站代收时
                                    if (info.getState().code() == 203) {
                                        info.setLocation(info.getPjCity());
                                    }
                                }
                                //跟踪轨迹信息序列成JSON（由于infoTrace 加上了@Transient使用cod字段替代）
                                info.setCod(JsonUtils.toJson(traces));
                                info.setModifyDate(new Date());
                                plist.add(info);
                            }
                        }
                    }
                    if (plist.size() > 0) {
                        infoService.updateListInfo(plist); //更新快递信息
                    }
                }
            }
        }
        countDownLatch.countDown();
    }

    @Override
    public void onFailure(Throwable throwable) {
        countDownLatch.countDown();
        LOGGER.error(throwable.getMessage());
    }

    /**
     * 同步快递的流转状态
     *
     * @param scanType String
     * @return String
     */
    private String synAction(String scanType, String remark) {
        String action = "";
        if ("收件".equals(scanType)) {
            action = "已揽收或已收件";
        } else if ("发件".equals(scanType) && StringUtils.contains(remark, "正发往")) {
            action = "在途中";
        } else if ("到件".equals(scanType) && StringUtils.contains(remark, "转运")) {
            action = "在途中";
        } else if ("到件".equals(scanType) && !StringUtils.contains(remark, "转运")) {
            action = "到达派件城市";
        } else if ("派件".equals(scanType) && StringUtils.contains(remark, "正在派件")) {
            action = "派件中";
        } else if ("入柜/入库".equals(scanType) || "入库/入柜".equals(scanType)) {
            action = "已到达合作点/已放入快递柜/自提柜/驿站代收";
        } else if ("签收".equals(scanType) || "用户提货".equals(scanType) || "小件员取出".equals(scanType)) {
            action = "已签收";
        }
        return action;
    }

    /**
     * 解析签收人
     *
     * @param acceptStation 站点操作描述
     * @return 签收人
     */
    private String getSignUser(String acceptStation) {
        if (StringUtils.isNotBlank(acceptStation) && StringUtils.contains(acceptStation, YQS) && !StringUtils.contains(acceptStation, "签收人凭取货码签收")) {
            String signUser=StringUtils.substringBetween(acceptStation, "，", YQS);
        	 if(StringUtils.isNotBlank(signUser)){
        		 signUser=signUser.trim();
             }
            return signUser;
        }
        return "签收人凭取货码签收";
    }

}