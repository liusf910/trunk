package com.ln.tms.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Courier - 快递公司
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_courier")
public class Courier extends BaseBean {

    @Id
    private String shipperCode;
    /**
     * 名称
     */
    private String courierName;
    /**
     * 分配的appKey
     */
    private String appKey;
    /**
     * 分配的secretKey
     */
    private String secretKey;
    /**
     * 分配的方法名
     */
    private String method;
    /**
     * 注册标识
     */
    private String userId;
    /**
     * URL
     */
    private String apiUrl;
    /**
     * 版本
     */
    private String apiVersion;
    /**
     * 格式
     */
    private String format;
    
    /**
     * 判断是否选中之用
     */
    @Transient
    private Boolean isSelected = false;

    public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Courier() {
    }

    public Courier(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName == null ? null : courierName.trim();
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}