package com.ln.tms.sync.ems;

import com.ln.tms.bean.EmsTrace;
import com.ln.tms.bean.EmsTrace.Traces;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;
import com.ln.tms.sync.IFutureCallback;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
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
 * EmsIFutureCallback
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings({"SpellCheckingInspection", "Duplicates"})
public class EmsIFutureCallback implements IFutureCallback<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmsIFutureCallback.class);

    private Info info;

    private CountDownLatch countDownLatch;

    private ConcurrentLinkedQueue<Info> infoQueue;

    public EmsIFutureCallback(Info info, CountDownLatch countDownLatch, ConcurrentLinkedQueue<Info> infoQueue) {
        this.info = info;
        this.countDownLatch = countDownLatch;
        this.infoQueue = infoQueue;
    }

    @Override
    public void onSuccess(String data) {
        if (StringUtils.isNotBlank(data)) {
            String logisticCode = info.getLogisticCode();
            // 获取物流流转信息
            EmsTrace EmsTrace = JsonUtils.toObject(data, EmsTrace.class);
            List<Traces> TLList = EmsTrace.getTraces();
            if (CollectionUtils.isEmpty(TLList) || TLList.size() == 0) {
                LOGGER.warn("EMS快递，单号:" + logisticCode + "暂没有物流流转信息");
            } else {
                // 物流流转信息
                List<Trace> traces = new ArrayList<>();
                for (Traces traceBean : TLList) {
                    Trace infoTrace = new Trace();
                    // 快递单号
                    infoTrace.setLogisticCode(logisticCode);
                    // 站点操作时间
                    infoTrace.setAcceptTime(traceBean.getAcceptTime());
                    // 站点操作描述
                    infoTrace.setAcceptStation(traceBean.getRemark().replace(
                            "\n", ""));
                    // 根据站点操作描述推导出当前到达的地点
                    infoTrace.setAcceptLocation(traceBean.getAcceptAddress());
                    // 根据站点操作描述推导出当前状态描述
                    infoTrace.setAction(this.synAction(traceBean.getRemark()));
                    traces.add(infoTrace);
                }
                if (CollectionUtils.isNotEmpty(traces) && info != null) { // 快递信息存在则更新
                    // 第一条轨迹信息获取揽件操作时间
                    info.setTookTime(DateUtils.stringToDate(traces.get(0)
                            .getAcceptTime()));
                    // 最后一条轨迹信息操作时间
                    info.setLastTime(DateUtils.stringToDate(traces.get(
                            traces.size() - 1).getAcceptTime()));
                    // 根据跟踪信息站点操作描述判断快递当前状态
                    info.setState(StateType.describeOf(traces.get(
                            traces.size() - 1).getAction()));
                    // 遍历轨迹信息如果在到达派件城市设置派件城市
                    for (Trace trace : traces) {
                        if (trace.getAction().equals(
                                StateType.PJZ.getDescribe())) {
                            info.setPjCity(trace.getAcceptLocation());
                        }
                    }
                    // 快递已签收状态
                    if (info.getState().code() == 3) {
                        // 收件人签收时间(来自跟踪信息)
                        info.setSignTime(info.getLastTime());
                        // 设置签收人(根据站点操作描述获取)
                        info.setSignUser(this.getSignUser(traces.get(
                                traces.size() - 1).getAcceptStation()));
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
                        info.setLocation(traces.get(traces.size() - 1)
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
                    info.setCod(JsonUtils.toJson(traces));
                    info.setModifyDate(new Date());
                    // 将处理结果放入到队列中
                    infoQueue.add(info);
                    traces = null;
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
     * @param remark String
     * @return String
     */
    private String synAction(String remark) {
        String action = "";
        if (StringUtils.contains(remark, "已收件")) {
            action = "已揽收或已收件";
        } else if (StringUtils.contains(remark, "安排投递")) {
            action = "派件中";
        } else if (StringUtils.contains(remark, "投递并签收")
                || StringUtils.contains(remark, "客户自提并签收")
                || "妥投".equals(remark) || StringUtils.contains(remark, "妥投，")) {
            action = "已签收";
        } else if (StringUtils.contains(remark, "未妥投")) {
            action = "问题件";
        } else if (StringUtils.contains(remark, "退回 妥投")) {
            action = "拒收或退件";
        } else {
            action = "在途中";
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
        if (StringUtils.isNotBlank(acceptStation)
                && (StringUtils.contains(acceptStation, "投递并签收") || StringUtils
                .contains(acceptStation, "客户自提并签收"))) {
            return StringUtils.substring(acceptStation,
                    acceptStation.indexOf("签收人：") + 4).trim();
        } else if (StringUtils.isNotBlank(acceptStation)
                && StringUtils.contains(acceptStation, "妥投，")) {
            return StringUtils.substring(acceptStation,
                    acceptStation.indexOf("妥投，") + 3).trim();
        }
        return "";
    }

}