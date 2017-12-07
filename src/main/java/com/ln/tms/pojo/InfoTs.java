package com.ln.tms.pojo;

import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.TookType;

import javax.persistence.Table;
import java.util.Date;

/**
 * InfoTs - 快递导入信息表
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_info_ts")
public class InfoTs extends BaseBean {

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
    private Date fileTookTime;

    /**
     * 签收时间
     */
    private Date fileSignTime;

    /**
     * 配送状态
     */
    private OrderType orderStatus;

    /**
     * 签收的json拼接串
     */
    private String signStr;

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

    public TookType getTookReason() {
        return tookReason;
    }

    public Date getFileTookTime() {
        return fileTookTime;
    }

    public void setFileTookTime(Date fileTookTime) {
        this.fileTookTime = fileTookTime;
    }

    public Date getFileSignTime() {
        return fileSignTime;
    }

    public void setFileSignTime(Date fileSignTime) {
        this.fileSignTime = fileSignTime;
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
        InfoTs infoTs = (InfoTs) obj;
        return getLogisticCode() != null ? getLogisticCode().equals(infoTs.getLogisticCode()) : false;
    }
}