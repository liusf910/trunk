package com.ln.tms.bean;

import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.StateType;
import com.ln.tms.enums.TookType;
import com.ln.tms.util.TmsUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * InfoWhere - 查询条件
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class InfoWhere {
    /**
     * 快递公司ID
     */
    private String shipperCode;
    /**
     * 快递单号
     */
    private String logisticCode;
    /**
     * 发货仓库代码
     */
    private String storageCode;
    /**
     * 配送状态(文件导入)
     */
    private OrderType orderState;
    /**
     * 揽收超时原因(文件导入)
     */
    private TookType tookReason;
    /**
     * 签收超时原因(文件导入)
     */
    private SignType signReason;
    /**
     * 物流状态(0-订单生成，1-已揽收，2-在途中，201-到达派件城市，202-派件中，211-已放入快递柜或驿站，3-已签收，311-已取出快递柜或驿站，4-问题件，401-发货无信息，402-超时未签收，403-超时未更新，404-拒收（退件），412-快递柜或驿站超时未取)
     */
    private StateType state;
    /**
     * 发货日期（开始）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shipmentsTimeStart;
    /**
     * 发货日期（结束）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shipmentsTimeEnd;
    /**
     * 计划到货日期开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planTimeStart;
    /**
     * 计划到货日期结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planTimeEnd;
    /**
     * 快递员揽件时间(来自文件导入开始)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tookTimeStart;
    /**
     * 快递员揽件时间(来自文件导入结束)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tookTimeEnd;
    /**
     * 收件人签收时间(来自文件导入开始)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTimeStart;
    /**
     * 收件人签收时间(来自文件导入结束)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTimeEnd;
    /**
     * 付款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTimeStart;
    /**
     * 付款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTimeEnd;
    /**
     * 今日到达
     */
    private boolean signToday;
    /**
     * 揽收超时/待揽超时
     */
    private boolean lsOver;
    /**
     * 配送超时
     */
    private boolean psOver;
    /**
     * 签收超时
     */
    private boolean qsOver;
    /**
     * 排序字段
     */
    private String orderField;
    /**
     * 排序
     */
    private String orderDirection;
    /**
     * 待签
     */
    private boolean signFor;
    /**
     * 待签超时天数
     */
    private String dqOver;
    /**
     * 待揽超时天数
     */
    private String dlsOver;
    /**
     * 人工导入早于系统对接24h
     */
    private boolean rgxtFlag;
    /**
     * 20点后发货当天揽收
     */
    private boolean fhTookFlag;
    
    /**
     * 付款时间早于揽收时间48h
     */
    private boolean fkzlsFlag;
    /**
     * 揽收超时原因为空
     */
    private boolean tookReasonNull;
    /**
     * 签收超时原因为空
     */
    private boolean signReasonNull;
    /**
     * 用户id(快递公司用户有该字段)
     */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDlsOver() {
        return dlsOver;
    }

    public void setDlsOver(String dlsOver) {
        this.dlsOver = dlsOver;
    }

    public boolean isRgxtFlag() {
        return rgxtFlag;
    }

    public void setRgxtFlag(boolean rgxtFlag) {
        this.rgxtFlag = rgxtFlag;
    }

    public boolean isFhTookFlag() {
        return fhTookFlag;
    }

    public void setFhTookFlag(boolean fhTookFlag) {
        this.fhTookFlag = fhTookFlag;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public OrderType getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderType orderState) {
        this.orderState = orderState;
    }

    public TookType getTookReason() {
        return tookReason;
    }

    public void setTookReason(TookType tookReason) {
        this.tookReason = tookReason;
    }

    public SignType getSignReason() {
        return signReason;
    }

    public void setSignReason(SignType signReason) {
        this.signReason = signReason;
    }

    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public Date getShipmentsTimeStart() {
        return shipmentsTimeStart;
    }

    public void setShipmentsTimeStart(Date shipmentsTimeStart) {
        this.shipmentsTimeStart = shipmentsTimeStart;
    }

    public Date getShipmentsTimeEnd() {
        return shipmentsTimeEnd;
    }

    public void setShipmentsTimeEnd(Date shipmentsTimeEnd) {
        this.shipmentsTimeEnd = shipmentsTimeEnd;
    }

    public Date getPlanTimeStart() {
        return planTimeStart;
    }

    public void setPlanTimeStart(Date planTimeStart) {
        this.planTimeStart = planTimeStart;
    }

    public Date getPlanTimeEnd() {
        return planTimeEnd;
    }

    public void setPlanTimeEnd(Date planTimeEnd) {
        this.planTimeEnd = planTimeEnd;
    }

    public Date getTookTimeStart() {
        return tookTimeStart;
    }

    public void setTookTimeStart(Date tookTimeStart) {
        this.tookTimeStart = tookTimeStart;
    }

    public Date getTookTimeEnd() {
        return tookTimeEnd;
    }

    public void setTookTimeEnd(Date tookTimeEnd) {
        this.tookTimeEnd = tookTimeEnd;
    }

    public Date getSignTimeStart() {
        return signTimeStart;
    }

    public void setSignTimeStart(Date signTimeStart) {
        this.signTimeStart = signTimeStart;
    }

    public Date getSignTimeEnd() {
        return signTimeEnd;
    }

    public void setSignTimeEnd(Date signTimeEnd) {
        this.signTimeEnd = signTimeEnd;
    }

    public Date getPayTimeStart() {
		return payTimeStart;
	}

	public void setPayTimeStart(Date payTimeStart) {
		this.payTimeStart = payTimeStart;
	}

	public Date getPayTimeEnd() {
		return payTimeEnd;
	}

	public void setPayTimeEnd(Date payTimeEnd) {
		this.payTimeEnd = payTimeEnd;
	}

	public boolean getLsOver() {
        return lsOver;
    }

    public void setLsOver(boolean lsOver) {
        this.lsOver = lsOver;
    }

    public boolean isPsOver() {
        return psOver;
    }

    public void setPsOver(boolean psOver) {
        this.psOver = psOver;
    }

    public boolean isQsOver() {
        return qsOver;
    }

    public void setQsOver(boolean qsOver) {
        this.qsOver = qsOver;
    }

    public boolean isSignToday() {
        return signToday;
    }

    public void setSignToday(boolean signToday) {
        this.signToday = signToday;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public boolean isSignFor() {
        return signFor;
    }

    public void setSignFor(boolean signFor) {
        this.signFor = signFor;
    }

    public String getDqOver() {
        return dqOver;
    }

    public void setDqOver(String dqOver) {
        this.dqOver = dqOver;
    }

    public boolean isTookReasonNull() {
        return tookReasonNull;
    }

    public void setTookReasonNull(boolean tookReasonNull) {
        this.tookReasonNull = tookReasonNull;
    }

    public boolean isSignReasonNull() {
        return signReasonNull;
    }

    public void setSignReasonNull(boolean signReasonNull) {
        this.signReasonNull = signReasonNull;
    }

    /**
     * 快递公司Name
     */
    private String courierName;

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

	public boolean getFkzlsFlag() {
		return fkzlsFlag;
	}

	public void setFkzlsFlag(boolean fkzlsFlag) {
		this.fkzlsFlag = fkzlsFlag;
	}
    
    

}