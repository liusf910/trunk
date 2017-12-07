package com.ln.tms.sync.yuantong;

import com.fasterxml.jackson.databind.JsonNode;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;
import com.ln.tms.sync.IFutureCallback;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * YuanTongIFutureCallback
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings({"Duplicates", "ConstantConditions"})
public class YuanTongIFutureCallback implements IFutureCallback<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(YuanTongIFutureCallback.class);
    private static final String WAYBILL_NO = "Waybill_No";
    private static final String UPLOAD_TIME = "Upload_Time";
    private static final String PROCESS_INFO = "ProcessInfo";
    private static final String QSR = "签收人:";
    private static final String YQS = "已签收";

    private CountDownLatch countDownLatch;

    private ConcurrentLinkedQueue<Info> infoQueue;

    private Info info;

    public YuanTongIFutureCallback(CountDownLatch countDownLatch, ConcurrentLinkedQueue<Info> infoQueue, Info info) {
        this.countDownLatch = countDownLatch;
        this.infoQueue = infoQueue;
        this.info = info;
    }

    @Override
    public void onSuccess(String data) {
        if (StringUtils.isNotBlank(data)) {
            if (StringUtils.contains(data, WAYBILL_NO) && StringUtils.contains(data, UPLOAD_TIME) && StringUtils.contains(data, PROCESS_INFO)) {
                JsonNode jsonNode = JsonUtils.toTree(data);
                //轨迹跟踪信息
                List<Trace> traces = new ArrayList<>();
                Trace infoTrace;
                for (int i = 0; i < jsonNode.size(); i++) {
                    JsonNode node = jsonNode.get(i);
                    infoTrace = new Trace();
                    infoTrace.setLogisticCode(node.get(WAYBILL_NO).asText());
                    infoTrace.setAcceptTime(node.get(UPLOAD_TIME).asText());
                    infoTrace.setAcceptStation(node.get(PROCESS_INFO).asText());
                    infoTrace.setAcceptLocation(getAcceptLocation(infoTrace.getAcceptStation()));
                    infoTrace.setAction(getAction(infoTrace.getAcceptStation()));
                    traces.add(infoTrace);
                }
                //第一条轨迹信息获取揽件时间
                info.setTookTime(DateUtils.stringToDate(traces.get(0).getAcceptTime()));
                //最后一条轨迹信息
                info.setLastTime(DateUtils.stringToDate(traces.get(traces.size() - 1).getAcceptTime()));
                //根据站点操作描述判断快递状态
                info.setState(StateType.describeOf(traces.get(traces.size() - 1).getAction()));
                //设置派件城市
                for (Trace trace : traces) {
                    if (trace.getAction().equals(StateType.PJZ.getDescribe())) {
                        info.setPjCity(getCity(trace.getAcceptLocation()));
                    }
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
                    info.setLocation(getCity(traces.get(traces.size() - 1).getAcceptLocation()));
                    info.setAttritTime(null);
                    info.setOverDay(null);
                    info.setAttritDay(null);
                    //在快递柜自提柜驿站代收时
                    if (info.getState().code() == 203) {
                        info.setLocation(info.getPjCity());
                    }
                }
                //跟踪轨迹信息序列成Json
                info.setCod(JsonUtils.toJson(traces));
                info.setModifyDate(new Date());
                //将处理结果放入到队列中
                infoQueue.add(info);
                traces = null;
            } else {
                LOGGER.warn("圆通单号={},{}", info.getLogisticCode(), data);
            }
        } else {
            LOGGER.warn("圆通单号={}流转信息为空", info.getLogisticCode());
        }
        countDownLatch.countDown();
    }

    @Override
    public void onFailure(Throwable throwable) {
        countDownLatch.countDown();
        LOGGER.error(throwable.getMessage());
    }

    /**
     * 解析签收人
     *
     * @param acceptStation 站点操作描述
     * @return 签收人
     */
    private String getSignUser(String acceptStation) {
        if (StringUtils.isNotBlank(acceptStation)
                && StringUtils.contains(acceptStation, QSR)
                && StringUtils.contains(acceptStation, YQS)) {
            return StringUtils.substringBetween(acceptStation, QSR, YQS).trim();
        }
        return acceptStation;
    }

    /**
     * 解析省市区县
     *
     * @param location String
     * @return String
     */
    private String getCity(String location) {
        int province = location.lastIndexOf(TmsUtils.PROVINCE);
        int city = location.lastIndexOf(TmsUtils.CITY);
        int district = location.lastIndexOf(TmsUtils.DISTRICT);
        int county = location.lastIndexOf(TmsUtils.COUNTY);
        String cs = "";
        if (province != -1) {
            cs = StringUtils.substring(location, 0, province + 1);
        }
        if (city != -1) {
            cs = StringUtils.substring(location, 0, city + 1);
        }
        if (district != -1) {
            cs = StringUtils.substring(location, 0, district + 1);
        }
        if (county != -1) {
            cs = StringUtils.substring(location, 0, county + 1);
        }
        if (StringUtils.isBlank(cs)) {
            return location;
        }
        return cs;
    }

    /**
     * 根据站点操作描述获取当前状态
     *
     * @param acceptStation 站点操作描述
     * @return String
     */
    private String getAction(String acceptStation) {
        if (StringUtils.contains(acceptStation, TmsUtils.YT_YSJ)) {
            return StateType.YLS.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.YT_YFC)
                || StringUtils.contains(acceptStation, TmsUtils.YT_HFY)
                || StringUtils.contains(acceptStation, TmsUtils.YT_YCB)
                || StringUtils.contains(acceptStation, TmsUtils.YT_YDB)
                || StringUtils.contains(acceptStation, TmsUtils.YT_YSR)
                || StringUtils.contains(acceptStation, TmsUtils.YT_HSR)
                || StringUtils.contains(acceptStation, TmsUtils.YT_YQF)) {
            return StateType.ZTZ.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.YT_PJZ)) {
            return StateType.PJZ.getDescribe();
        } else if (!StringUtils.contains(acceptStation, TmsUtils.YT_YQS)
                && (StringUtils.contains(acceptStation, TmsUtils.YT_YZ)
                || StringUtils.contains(acceptStation, TmsUtils.YT_ZT)
                || StringUtils.contains(acceptStation, TmsUtils.YT_HZ))) {
            return StateType.FYZ.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.YT_YQS)) {
            return StateType.YQS.getDescribe();
        } else {
            return StateType.WTJ.getDescribe();
        }
    }

    /**
     * 根据站点操作描述获取到达地
     *
     * @param acceptStation 站点操作描述
     * @return String
     */
    private String getAcceptLocation(String acceptStation) {
        String[] split = acceptStation.split(TmsUtils.YT_SPLIT);
        return StringUtils.substring(split[0], split[0].indexOf(TmsUtils.YT_L) + 1, split[0].lastIndexOf(TmsUtils.YT_R));

    }


}