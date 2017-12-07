package com.ln.tms.sync.shentong;


import com.fasterxml.jackson.databind.JsonNode;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;
import com.ln.tms.sync.IFutureCallback;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.collections.CollectionUtils;
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
 * ShenTongIFutureCallback
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("Duplicates")
public class ShenTongIFutureCallback implements IFutureCallback<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShenTongIFutureCallback.class);

    private CountDownLatch countDownLatch;

    private ConcurrentLinkedQueue<Info> infoQueue;

    private Info info;

    public ShenTongIFutureCallback(CountDownLatch countDownLatch, ConcurrentLinkedQueue<Info> infoQueue, Info info) {
        this.countDownLatch = countDownLatch;
        this.infoQueue = infoQueue;
        this.info = info;
    }

    @Override
    public void onSuccess(String data) {
        if (StringUtils.isNotBlank(data)) {
            JsonNode jsonNode = JsonUtils.toTree(data);
            if (!(jsonNode.get("StatusCode").asText().equals("OK")) || !(jsonNode.get("Status").asText().equals("true"))) {
                LOGGER.warn("申通快递接口调用失败,接口失败返回编码{},原因{}", jsonNode.get("StatusCode"), jsonNode.get("StatusMessage"));
            } else {
                List<Trace> traces = new ArrayList<>();
                JsonNode nodeData = jsonNode.get("Data").get(0);
                JsonNode nodeScanList = nodeData.get("ScanList");
                for (int i = 0; i < nodeScanList.size(); i++) {
                    Trace infoTrace = new Trace();
                    infoTrace.setLogisticCode(nodeData.get("Id").asText());
                    infoTrace.setAcceptTime(nodeScanList.get(i).get("UploadDate").asText());
                    infoTrace.setAcceptStation(nodeScanList.get(i).get("ScanType").asText());
                    infoTrace.setAcceptLocation(nodeScanList.get(i).get("UploadSiteName").asText());
                    infoTrace.setAction(getAction(infoTrace.getAcceptStation()));
                    if ("YQS".equals(StateType.describeOf(infoTrace.getAction()).toString()))
                        traces.clear();
                    traces.add(infoTrace);
                }
                if (CollectionUtils.isNotEmpty(traces)) {
                    //第一条轨迹信息获取揽件时间
                    info.setTookTime(DateUtils.stringToDate(traces.get(traces.size() - 1).getAcceptTime()));
                    //设置派件城市
                    for (Trace trace : traces) {
                        if ("YQS".equals(StateType.describeOf(trace.getAction()).toString())) {
                            //最后一条轨迹信息
                            info.setLastTime(DateUtils.stringToDate(trace.getAcceptTime()));
                            //根据站点操作描述判断快递状态
                            info.setState(StateType.describeOf(trace.getAction()));
                        } else {
                            //最后一条轨迹信息
                            if (null == info.getLastTime())
                                info.setLastTime(DateUtils.stringToDate(traces.get(0).getAcceptTime()));
                            info.setState(StateType.describeOf(trace.getAction()));
                        }
                        if (trace.getAction().equals(StateType.PJZ.getDescribe())) {
                            info.setPjCity(getCity(trace.getAcceptLocation()));
                        }
                    }
                    //快递已签收状态
                    if (info.getState().code() == 3) {
                        //收件人签收时间(来自跟踪信息)
                        info.setSignTime(info.getLastTime());
                        //设置签收人(根据站点操作描述获取)
                        info.setSignUser(nodeData.get("SignMan").asText());
                        //到达城市
                        info.setLocation(info.getPjCity());
                        //时效（实际耗时）第一条信息时间到签收时间差
                        if (info.getSignTime() == null || info.getTookTime() == null) {
                            info.setAttritTime(0L);
                        } else {
                            info.setAttritTime(info.getSignTime().getTime() - info.getTookTime().getTime());
                        }
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
                        info.setLocation(getCity(traces.get(0).getAcceptLocation()));
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
                    LOGGER.warn("申通快递,单号={}暂没有物流流转信息", info.getLogisticCode());
                }
            }
        } else {
            LOGGER.warn("申通单号={},{}", info.getLogisticCode(), data);
        }
        countDownLatch.countDown();
    }

    @Override
    public void onFailure(Throwable throwable) {
        countDownLatch.countDown();
        LOGGER.error(throwable.getMessage());
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
        if (StringUtils.contains(acceptStation, TmsUtils.ST_SJ)) {
            return StateType.YLS.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.ST_FJ)) {
            return StateType.ZTZ.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.ST_PJ)) {
            return StateType.PJZ.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.ST_DJ)) {
            return StateType.FYZ.getDescribe();
        } else if (StringUtils.contains(acceptStation, TmsUtils.ST_QS)) {
            return StateType.YQS.getDescribe();
        } else {
            return StateType.WTJ.getDescribe();
        }
    }
}