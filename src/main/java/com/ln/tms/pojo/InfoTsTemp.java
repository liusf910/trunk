package com.ln.tms.pojo;

import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.TookType;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * InfoTsTemp - 快递导入临时表
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_info_ts_temp")
public class InfoTsTemp extends BaseBean {

    /**
     * 外键ID快递公司编号
     */
    private String shipperCode;

    /**
     * 快递单号
     */
    private String logisticCode;

    /**
     * 超时
     */
    private String timeOut;

    /**
     * 揽收超时原因
     */
    private TookType tookReason;

    /**
     * 签收超时原因
     */
    private SignType signReason;

    /**
     * 揽收时间
     */
    private String fileTookTime;

    /**
     * 签收时间
     */
    private String fileSignTime;

    /**
     * 配送状态
     */
    private OrderType orderStatus;

    /**
     * 签收多原因，状态，时间拼接字符串
     */
    private String signStr;

    /**
     * 导入的标识
     */
    private String flag;

    /**
     * 快递名称
     */
    @Transient
    private String courierName;

    /**
     * 发货时间
     */
    @Transient
    private String shipmentsTime;

    /**
     * 发货日期
     */
    @Transient
    private String ShipmentsDate;

    /**
     * 订单号
     */
    @Transient
    private String orderCode;

    /**
     * 客户姓名
     */
    @Transient
    private String customName;

    /**
     * 应到货时间
     */
    @Transient
    private String planTime;

    /**
     * 超时天数
     */
    @Transient
    private Integer overDay;

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode == null ? null : shipperCode.trim();
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode == null ? null : logisticCode.trim();
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }


    public String getFileTookTime() {
        return fileTookTime;
    }

    public void setFileTookTime(String fileTookTime) {
        this.fileTookTime = fileTookTime;
    }

    public String getFileSignTime() {
        return fileSignTime;
    }

    public void setFileSignTime(String fileSignTime) {
        this.fileSignTime = fileSignTime;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getShipmentsTime() {
        return shipmentsTime;
    }

    public void setShipmentsTime(String shipmentsTime) {
        this.shipmentsTime = shipmentsTime;
    }

    public String getShipmentsDate() {
        return ShipmentsDate;
    }

    public void setShipmentsDate(String shipmentsDate) {
        ShipmentsDate = shipmentsDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getOverDay() {
        return overDay;
    }

    public void setOverDay(Integer overDay) {
        this.overDay = overDay;
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

    public OrderType getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderType orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSignStr() {
        return signStr;
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr == null ? null : signStr.trim();
    }

    /**
     * 重写hashCode方法
     *
     * @return HashCode
     */
    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += getLogisticCode() != null ? getLogisticCode().hashCode() * 31 : 0;
        return hashCode;
    }

    /**
     * 重写equals方法
     *
     * @param obj 对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        InfoTsTemp infoTsTemp = (InfoTsTemp) obj;
        return getLogisticCode() != null ? getLogisticCode().equals(infoTsTemp.getLogisticCode()) : false;
    }
}