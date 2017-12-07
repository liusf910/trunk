package com.ln.tms.sync.jingdong;

import com.jd.open.api.sdk.response.etms.TraceApiDto;
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
 * JingDongIFutureCallback - 京东执行结果回调
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("Duplicates")
public class JingDongIFutureCallback implements IFutureCallback<List<TraceApiDto>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JingDongIFutureCallback.class);

    private CountDownLatch countDownLatch;

    private ConcurrentLinkedQueue<Info> infoQueue;

    private Info info;

    public JingDongIFutureCallback(CountDownLatch countDownLatch, ConcurrentLinkedQueue<Info> infoQueue, Info info) {
        this.countDownLatch = countDownLatch;
        this.infoQueue = infoQueue;
        this.info = info;
    }

    @Override
    public void onSuccess(List<TraceApiDto> traceApiDtos) {
        if (CollectionUtils.isNotEmpty(traceApiDtos)) {
            List<Trace> traces = new ArrayList<>();
            Trace infoTrace;
            for (TraceApiDto traceApiDto : traceApiDtos) {
                infoTrace = new Trace();
                //快递单号
                infoTrace.setLogisticCode(info.getLogisticCode());
                //站点操作时间
                infoTrace.setAcceptTime(traceApiDto.getOpeTime());
                //站点操作描述
                infoTrace.setAcceptStation(traceApiDto.getOpeTitle());
                //根据站点操作描述推导出当前到达的地点
                infoTrace.setAcceptLocation(traceApiDto.getOpeRemark());
                //根据站点操作描述推导出当前状态描述
                infoTrace.setAction(getAction(traceApiDto.getOpeTitle()));
                traces.add(infoTrace);
            }
            //第一条轨迹信息获取揽件操作时间
            info.setTookTime(DateUtils.strToDate(traces.get(0).getAcceptTime()));
            //最后一条轨迹信息操作时间
            info.setLastTime(DateUtils.strToDate(traces.get(traces.size() - 1).getAcceptTime()));
            //根据跟踪信息站点操作描述判断快递当前状态
            info.setState(StateType.describeOf(traces.get(traces.size() - 1).getAction()));
            //遍历轨迹信息如果在派件中设置派件城市
            for (Trace trace : traces) {
                if (trace.getAction().equals(StateType.DPJ.getDescribe())) {
                    info.setPjCity(trace.getAcceptLocation());
                }
            }
            //快递已签收状态
            if (info.getState().code() == 3) {
                //收件人签收时间(来自跟踪信息)
                info.setSignTime(info.getLastTime());
                //设置签收人(根据站点操作描述获取)
                info.setSignUser(null);
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
            } else {//其他状态
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
            //跟踪轨迹信息序列成json
            info.setCod(JsonUtils.toJson(traces));
            info.setModifyDate(new Date());
            infoQueue.add(info);
            traces = null;
        } else {
            LOGGER.warn("京东单号={}流转信息为空", info.getLogisticCode());
        }
        countDownLatch.countDown();
    }

    @Override
    public void onFailure(Throwable throwable) {
        countDownLatch.countDown();
        LOGGER.error(throwable.getMessage());
    }

    /**
     * 根据站点操作描述获取当前状态
     *
     * @param opeTitle 站点操作描述
     * @return String
     */
    private String getAction(String opeTitle) {
        if (StringUtils.contains(opeTitle, "快递签收")) {
            return StateType.YLS.getDescribe();
        } else if (StringUtils.contains(opeTitle, "分拣中心验货")
                || StringUtils.contains(opeTitle, "分拣中心发货")
                || StringUtils.contains(opeTitle, "站点收货")
                || StringUtils.contains(opeTitle, "站点验货")
                || StringUtils.contains(opeTitle, "配送员收货")) {
            return StateType.ZTZ.getDescribe();
        } else if (StringUtils.contains(opeTitle, "妥投")) {
            return StateType.YQS.getDescribe();
        } else {
            return StateType.WTJ.getDescribe();
        }

    }

}
