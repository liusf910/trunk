package com.ln.tms.sync.zhongtong;

import com.ln.tms.bean.Trace;
import com.ln.tms.bean.ZhongTongTrace;
import com.ln.tms.bean.ZhongTongTrace.ZhongTongData;
import com.ln.tms.bean.ZhongTongTrace.ZhongTongData.Traces;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;
import com.ln.tms.service.InfoService;
import com.ln.tms.sync.IFutureCallback;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * ZhongTongIFutureCallback - 中通执行结果回调
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class ZhongTongIFutureCallback implements IFutureCallback<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhongTongIFutureCallback.class);
    private static final String QSR = "签收人:";
    private static final String YQS = "已签收";
    private static final String ZT = "";
    private CountDownLatch countDownLatch;

    private ConcurrentLinkedQueue<Info> infoQueue;

    private InfoService infoService;

    public ZhongTongIFutureCallback(InfoService infoService, CountDownLatch countDownLatch,ConcurrentLinkedQueue<Info> infoQueue) {
        this.countDownLatch = countDownLatch;
        this.infoService = infoService;
        this.infoQueue = infoQueue;
    }

    @Override
    public void onSuccess(String data) {
        if (StringUtils.isNotBlank(data)) {
            //获取物流流转信息
            ZhongTongTrace zhongTongTrace = JsonUtils.toObject(data, ZhongTongTrace.class);
            List<ZhongTongData> ztlist = zhongTongTrace.getData();
            for(ZhongTongData zhongTongData : ztlist){
                List<Traces> trlist = zhongTongData.getTraces();
               if(CollectionUtils.isEmpty(trlist)){
                   LOGGER.warn("中通快递，单号:" + zhongTongData.getBillcode() + "暂没有物流流转信息");
               }else {
                   //key=快递单号，value=轨迹信息
                   Map<String, List<Trace>> map = new HashMap<>();
                   Trace infoTrace;
                   for(Traces traces : trlist){
                       String key = zhongTongData.getBillcode();
                       if(map.containsKey(key)){
                           List<Trace> trace = map.get(key);
                           infoTrace = new Trace();
                           //快递单号
                           infoTrace.setLogisticCode(key);
                           //站点操作时间
                           infoTrace.setAcceptTime(traces.getScandate());
                           //站点操作描述
                           infoTrace.setAcceptStation(traces.getRemark().replace("\n", ""));
                           //根据站点操作描述推导出当前到达的地点
                           infoTrace.setAcceptLocation(traces.getPreornextsite());
                           //根据站点操作描述推导出当前状态描述
                           infoTrace.setAction(this.getAction(traces.getScantype()));
                           trace.add(infoTrace);
                       }else{
                           List<Trace> infoTraces = new ArrayList<>();
                           infoTrace = new Trace();
                           infoTrace.setLogisticCode(key);
                           infoTrace.setAcceptTime(traces.getScandate());
                           infoTrace.setAcceptStation(traces.getRemark().replace("\n", ""));
                           infoTrace.setAcceptLocation(traces.getPreornextsite());
                           infoTrace.setAction(this.getAction(traces.getScantype()));
                           infoTraces.add(infoTrace);
                           map.put(key, infoTraces);
                       }
                   }
                   //处理map
                   List<Info> infolist = new ArrayList<>();
                   for (Map.Entry<String, List<Trace>> stringListEntry : map.entrySet()) {
                       //快递单号
                       String logisticCode = stringListEntry.getKey();
                       //对应的快递跟踪轨迹信息
                       List<Trace> trace = map.get(logisticCode);
                       //先查后改
                       Info t = new Info();
                       t.setLogisticCode(logisticCode);
                       t.setShipperCode(ZT);
                       List<Info> infoList = infoService.queryByWhere(t);
                       if (null != infoList) { //快递信息存在则更新
                           for(Info info : infoList){
                                //第一条轨迹信息获取揽件操作时间
                               info.setTookTime(DateUtils.stringToDate(trace.get(0).getAcceptTime()));
                               //最后一条轨迹信息操作时间
                               info.setLastTime(DateUtils.stringToDate(trace.get(trace.size() - 1).getAcceptTime()));
                               //根据跟踪信息站点操作描述判断快递当前状态
                               info.setState(StateType.describeOf(trace.get(trace.size() - 1).getAction()));
                               //遍历轨迹信息如果在到达派件城市设置派件城市
                               for (Trace qtrace : trace) {
                                   if (qtrace.getAction().equals(StateType.DPJ.getDescribe())) {
                                       info.setPjCity(qtrace.getAcceptLocation());
                                   }
                               }
                               // 快递已签收状态
                               if (info.getState().code() == 3) {
                                   // 收件人签收时间(来自跟踪信息)
                                   info.setSignTime(info.getLastTime());
                                   // 设置签收人(根据站点操作描述获取)
                                   info.setSignUser(this.getSignUser(trace.get(
                                           trace.size() - 1).getAcceptStation()));
                                   // 到达城市
                                   info.setLocation(info.getPjCity());
                                   // 时效（实际耗时）第一条信息时间到签收时间差
                                   info.setAttritTime(info.getSignTime().getTime()
                                           - info.getTookTime().getTime());
                                   // 超时天数实际到达时间减去计划到达时间
                                   if (info.getSignTime() == null
                                           && info.getFileSignTime() != null) {
                                       info.setOverDay(DateUtils.getDateDifference(
                                               info.getPlanTime(), info.getFileSignTime()));
                                   } else if (info.getSignTime() != null
                                           && info.getFileSignTime() == null) {
                                       info.setOverDay(DateUtils.getDateDifference(
                                               info.getPlanTime(), info.getSignTime()));
                                   } else if (info.getSignTime() != null
                                           && info.getFileSignTime() != null) {
                                       if (DateUtils.getDateHours(info.getSignTime(),
                                               info.getFileSignTime()) < 24) {
                                           info.setOverDay(DateUtils.getDateDifference(
                                                   info.getPlanTime(),
                                                   info.getFileSignTime()));
                                       } else {
                                           info.setOverDay(DateUtils.getDateDifference(
                                                   info.getPlanTime(), info.getSignTime()));
                                       }
                                   }
                                   // 送货天数（在途天数）签收时间减发货时间（20点前做发运的，都当日开始计算，之后+1）
                                   DateTime shipmentsTime = new DateTime(
                                           info.getShipmentsTime());
                                   if (shipmentsTime.getHourOfDay() < 20) {
                                       info.setAttritDay(DateUtils.getDateDifference(
                                               info.getShipmentsTime(), info.getSignTime()));
                                   }
                                   if (shipmentsTime.getHourOfDay() >= 20) {
                                       info.setAttritDay(DateUtils.getDateDifference(
                                               info.getShipmentsTime(), info.getSignTime()) + 1);
                                   }
                               } else { // 其他状态
                                   info.setSignTime(null);
                                   info.setSignUser(null);
                                   info.setLocation(trace.get(trace.size() - 1)
                                           .getAcceptLocation());
                                   info.setAttritTime(null);
                                   info.setOverDay(null);
                                   info.setAttritDay(null);
                                   // 在快递柜自提柜驿站代收时
                                   if (info.getState().code() == 203) {
                                       info.setLocation(info.getPjCity());
                                   }
                               }
                               // 跟踪轨迹信息序列成JSON（由于infoTrace 加上了@Transient使用cod字段替代）
                               info.setCod(JsonUtils.toJson(trace));
                               info.setModifyDate(new Date());
                               // 将处理结果放入到队列中
                               infoQueue.add(info);
                               trace = null;
                           }
                       }
                   }
               }
            }
        }
        countDownLatch.countDown();

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    /**
     * 根据站点操作描述获取当前状态
     *
     * @param scanType 站点操作描述
     * @return String
     */
    private String getAction(String scanType) {
        if (StringUtils.contains(scanType, "收件")) {
            return StateType.YLS.getDescribe();
        } else if (StringUtils.contains(scanType, "发件")
                || StringUtils.contains(scanType, "到件")
                || StringUtils.contains(scanType,"问题件")) {
            return StateType.ZTZ.getDescribe();
        } else if(StringUtils.contains(scanType,"派件")){
            return StateType.PJZ.getDescribe();
        }else if (StringUtils.contains(scanType, "签收")) {
            return StateType.YQS.getDescribe();
        } else {
            return StateType.WTJ.getDescribe();
        }

    }

    /**
     * 解析签收人
     *
     * @param acceptStation 站点操作描述
     * @return 签收人
     */
    private String getSignUser(String acceptStation) {
        if (StringUtils.isNotBlank(acceptStation) && StringUtils.contains(acceptStation, YQS) && !StringUtils.contains(acceptStation, "签收人凭取货码签收")) {
            return StringUtils.substringBetween(acceptStation, "，", YQS).trim();
        }
        return "签收人凭取货码签收";
    }
}
