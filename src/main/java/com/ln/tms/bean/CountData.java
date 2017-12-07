package com.ln.tms.bean;

import java.io.Serializable;

/**
 * CountData - 统计信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class CountData implements Serializable {

    /**
     * 仓库编号
     */
    private String storageCode;

    /**
     * 快递编号
     */
    private String shipperCode;

    /**
     * 仓库名称
     */
    private String storageName;

    /**
     * 快递名称
     */
    private String courierName;

    /**
     * 总笔数
     */
    private Integer total;

    /**
     * 总揽收数量
     */
    private Integer tookTotal;
    /**
     * 揽收及时数
     */
    private Integer tookNum;
    /**
     * 妥投数
     */
    private Integer deliverNum;
    /**
     * 3天送达数量
     */
    private Integer threeDayNum;
    /**
     * 及时送达数
     */
    private Integer inTimeNum;
    /**
     * 揽收及时率
     */
    private double tookRate;
    /**
     * 妥投率
     */
    private double deliverRate;
    /**
     * 三天到达率
     */
    private double threeDayRate;
    /**
     * 及时率
     */
    private double inTimeRate;

    /**
     * 总天数
     */
    private Integer totalDay;

    /**
     * 平均送货天数
     */
    private double avgDay;

    /**
     * 到达省份
     */
    private String province;

    /**
     * 1天内送到的笔数
     */
    private Integer oneDaySignNum;

    /**
     * 2天内送到的笔数
     */
    private Integer twoDaySignNum;

    /**
     * 3天内送到的笔数
     */
    private Integer threeDaySignNum;

    /**
     * 大于3天到达笔数
     */
    private Integer gtThreeDaySignNum;

    /**
     * 统计发货开始时间
     */
    private String shipmentsStartDate;

    /**
     * 统计发货结束时间
     */
    private String shipmentsEndDate;

    /**
     * 类型
     */
    private String type;

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTookTotal() {
        return tookTotal;
    }

    public void setTookTotal(Integer tookTotal) {
        this.tookTotal = tookTotal;
    }

    public Integer getTookNum() {
        return tookNum;
    }

    public void setTookNum(Integer tookNum) {
        this.tookNum = tookNum;
    }

    public Integer getDeliverNum() {
        return deliverNum;
    }

    public void setDeliverNum(Integer deliverNum) {
        this.deliverNum = deliverNum;
    }

    public Integer getThreeDayNum() {
        return threeDayNum;
    }

    public void setThreeDayNum(Integer threeDayNum) {
        this.threeDayNum = threeDayNum;
    }

    public Integer getInTimeNum() {
        return inTimeNum;
    }

    public void setInTimeNum(Integer inTimeNum) {
        this.inTimeNum = inTimeNum;
    }

    public double getTookRate() {
        return tookRate;
    }

    public void setTookRate(double tookRate) {
        this.tookRate = tookRate;
    }

    public double getDeliverRate() {
        return deliverRate;
    }

    public void setDeliverRate(double deliverRate) {
        this.deliverRate = deliverRate;
    }

    public double getThreeDayRate() {
        return threeDayRate;
    }

    public void setThreeDayRate(double threeDayRate) {
        this.threeDayRate = threeDayRate;
    }

    public double getInTimeRate() {
        return inTimeRate;
    }

    public void setInTimeRate(double inTimeRate) {
        this.inTimeRate = inTimeRate;
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public double getAvgDay() {
        return avgDay;
    }

    public void setAvgDay(double avgDay) {
        this.avgDay = avgDay;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getOneDaySignNum() {
        return oneDaySignNum;
    }

    public void setOneDaySignNum(Integer oneDaySignNum) {
        this.oneDaySignNum = oneDaySignNum;
    }

    public Integer getTwoDaySignNum() {
        return twoDaySignNum;
    }

    public void setTwoDaySignNum(Integer twoDaySignNum) {
        this.twoDaySignNum = twoDaySignNum;
    }

    public Integer getThreeDaySignNum() {
        return threeDaySignNum;
    }

    public void setThreeDaySignNum(Integer threeDaySignNum) {
        this.threeDaySignNum = threeDaySignNum;
    }

    public Integer getGtThreeDaySignNum() {
        return gtThreeDaySignNum;
    }

    public void setGtThreeDaySignNum(Integer gtThreeDaySignNum) {
        this.gtThreeDaySignNum = gtThreeDaySignNum;
    }

    public String getShipmentsStartDate() {
        return shipmentsStartDate;
    }

    public void setShipmentsStartDate(String shipmentsStartDate) {
        this.shipmentsStartDate = shipmentsStartDate;
    }

    public String getShipmentsEndDate() {
        return shipmentsEndDate;
    }

    public void setShipmentsEndDate(String shipmentsEndDate) {
        this.shipmentsEndDate = shipmentsEndDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}