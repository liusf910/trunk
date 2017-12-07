package com.ln.tms.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tms_count_rate")
public class CountRate extends BaseBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 仓库编号
     */
    private String storageCode;

    /**
     * 快递编号
     */
    private String shipperCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 统计的日期
     */
    private Date countDate;

    /**
     * 统计总数量
     */
    private Integer shouldNum;


    /**
     * 实际数量
     */
    private Integer realNum;

    /**
     * 比率
     */
    private double sentRate;

    /**
     * 统计的类型(0、快递公司揽收率统计，1、快递公司签收率统计)
     */
    private String countType;

    /**
     * 到货天数
     */
    private Integer attritDay;

    /**
     * 快递公司名称
     */
    @Transient
    private String courierName;

    /**
     * 统计的开始时间
     */
    @Transient
    private String startDate;

    /**
     * 统计的结束时间
     */
    @Transient
    private String endDate;

    /**
     * 图表统计类型
     */
    @Transient
    private String type;

    /**
     * 统计的当前日期的前一天
     */
    @Transient
    private String beforDate;

    @Transient
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode == null ? null : storageCode.trim();
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode == null ? null : shipperCode.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Date getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }

    public Integer getShouldNum() {
        return shouldNum;
    }

    public void setShouldNum(Integer shouldNum) {
        this.shouldNum = shouldNum;
    }

    public Integer getRealNum() {
        return realNum;
    }

    public void setRealNum(Integer realNum) {
        this.realNum = realNum;
    }

    public double getSentRate() {
        return sentRate;
    }

    public void setSentRate(double sentRate) {
        this.sentRate = sentRate;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType == null ? null : countType.trim();
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeforDate() {
        return beforDate;
    }

    public void setBeforDate(String beforDate) {
        this.beforDate = beforDate;
    }

    public Integer getAttritDay() {
        return attritDay;
    }

    public void setAttritDay(Integer attritDay) {
        this.attritDay = attritDay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}