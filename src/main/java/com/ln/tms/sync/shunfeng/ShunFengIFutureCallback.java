package com.ln.tms.sync.shunfeng;

import com.ln.tms.bean.ShunFengTrace;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;
import com.ln.tms.service.InfoService;
import com.ln.tms.sync.IFutureCallback;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ShunFengIFutureCallback implements IFutureCallback<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShunFengIFutureCallback.class);
    private static final String SHUNFENG = "10091";
    private static final String IGNORE = "ignore";

    private InfoService infoService;

    private CountDownLatch countDownLatch;

    public ShunFengIFutureCallback(InfoService infoService, CountDownLatch countDownLatch) {
        this.infoService = infoService;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onSuccess(String data) {
        ShunFengTrace shunFengTrace = dataForShunFengTrace(data);
        if (!shunFengTrace.isSuccess()) {
            if (IGNORE.equals(shunFengTrace.getErrCode())) {
                LOGGER.warn(shunFengTrace.getErrMsg());
            } else {
                LOGGER.error("顺丰路由查询接口错误（错误编码：" + shunFengTrace.getErrCode() + "错误信息：" + shunFengTrace.getErrMsg() + ")");
            }
        } else {
            List<List<Trace>> result = shunFengTrace.getResult();
            //处理数据
            List<Info> plist = new ArrayList<>();
            for (List<Trace> traces : result) {
                if (traces.isEmpty()) {
                    continue;
                }
                //快递单号
                String logisticCode = traces.get(0).getLogisticCode();
                Info t = new Info();
                t.setLogisticCode(logisticCode);
                t.setShipperCode(SHUNFENG);
                List<Info> infoList = infoService.queryByWhere(t);
                //快递信息存在则更新
                if (null != infoList) {
                    for (Info info : infoList) {
                        //第一天轨迹信息获取揽件操作时间
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
                        } else {
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
                if (plist.size() > 0) {
                    infoService.updateListInfo(plist);
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

    private ShunFengTrace dataForShunFengTrace(String data) {
        ShunFengTrace shunFengTrace = new ShunFengTrace();

        try {
            //解析data
            Document doc = DocumentHelper.parseText(data);
            Element root = doc.getRootElement();
            //获取结果
            String resultStatus = root.element("Head").getText();
            if ("ERR".equals(resultStatus)) {
                String errCode = root.element("ERROR").attributeValue("code");
                String errMsg = root.element("ERROR").getText();
                shunFengTrace.setErrCode(errCode);
                ;
                shunFengTrace.setErrMsg(errMsg);
                shunFengTrace.setSuccess(false);
                return shunFengTrace;
            }
            List<List<Trace>> result = new ArrayList<>();
            List<Element> rountResponses = root.element("Body").elements();
            if (rountResponses.isEmpty()) {
                shunFengTrace.setErrCode(IGNORE);
                shunFengTrace.setErrMsg("全部为线下订单或不存在的订单,忽略更新");
                shunFengTrace.setSuccess(false);
                return shunFengTrace;
            }
            for (Element rountResponse : rountResponses) {
                List<Trace> traces = new ArrayList<>();
                List<Element> rounts = rountResponse.elements();
                if (rounts.isEmpty()) {
                    continue;
                }
                for (Element rount : rounts) {
                    Trace trace = new Trace();
                    trace.setLogisticCode(rountResponse.attributeValue("mailno") == null ? "" : rountResponse.attributeValue("mailno"));
                    trace.setAcceptTime(rount.attributeValue("accept_time") == null ? "" : rount.attributeValue("accept_time"));
                    trace.setAcceptLocation(rount.attributeValue("accept_address") == null ? "" : rount.attributeValue("accept_address"));
                    trace.setAction(rount.attributeValue("opcode") == null ? "" : this.synAction(rount.attributeValue("opcode")));
                    trace.setAcceptStation(rount.attributeValue("remark"));
                    traces.add(trace);
                }
                result.add(traces);
            }
            shunFengTrace.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shunFengTrace;
    }


    private String synAction(String opCode) {
        if ("50".equals(opCode) || "700".equals(opCode)) {
            return StateType.YLS.getDescribe();
        } else if ("30".equals(opCode) || "31".equals(opCode)) {
            return StateType.ZTZ.getDescribe();
        } else if ("44".equals(opCode) || "204".equals(opCode) || "70".equals(opCode) || "33".equals(opCode)) {
            return StateType.PJZ.getDescribe();
        } else if ("80".equals(opCode) || "8000".equals(opCode)) {
            return StateType.YQS.getDescribe();
        } else if ("".equals(opCode)) {
            return StateType.WTJ.getDescribe();
        } else {
            return "未录入的状态码";
        }
    }
}
