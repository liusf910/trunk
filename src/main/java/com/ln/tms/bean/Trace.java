package com.ln.tms.bean;

/**
 * Trace - 轨迹信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class Trace {

    /**
     * 快递单号
     */
    private String logisticCode;
    /**
     * 站点操作时间
     */
    private String acceptTime;
    /**
     * 站点操作描述
     */
    private String acceptStation;
    /**
     * 到达的地点
     */
    private String acceptLocation;
    /**
     * 当前状态描述
     */
    private String action;


    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return acceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation;
    }

    public String getAcceptLocation() {
        return acceptLocation;
    }

    public void setAcceptLocation(String acceptLocation) {
        this.acceptLocation = acceptLocation;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}