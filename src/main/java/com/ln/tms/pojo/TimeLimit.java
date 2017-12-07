package com.ln.tms.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TimeLimitMapper - 快递公司时限
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_time_limit")
public class TimeLimit extends BaseBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long limitId;
    /**
     * 仓库
     */
    private String warehouse;
    /**
     * 目的省
     */
    private String province;
    /**
     * 目的市
     */
    private String city;
    /**
     * 申通E物流
     */
    private Integer ste;
    /**
     * 圆通速递
     */
    private Integer yt;
    /**
     * 汇通快运
     */
    private Integer ht;
    /**
     * 中通速递
     */
    private Integer zt;
    /**
     * 韵达快递
     */
    private Integer yd;
    /**
     * 顺丰速运
     */
    private Integer sf;
    /**
     * EMS
     */
    private Integer ems;
    /**
     * 京东快递
     */
    private Integer jd;
    /**
     * 京东COD
     */
    private Integer jdcod;

    public Long getLimitId() {
        return limitId;
    }

    public void setLimitId(Long limitId) {
        this.limitId = limitId;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getSte() {
        return ste;
    }

    public void setSte(Integer ste) {
        this.ste = ste;
    }

    public Integer getYt() {
        return yt;
    }

    public void setYt(Integer yt) {
        this.yt = yt;
    }

    public Integer getHt() {
        return ht;
    }

    public void setHt(Integer ht) {
        this.ht = ht;
    }

    public Integer getZt() {
        return zt;
    }

    public void setZt(Integer zt) {
        this.zt = zt;
    }

    public Integer getYd() {
        return yd;
    }

    public void setYd(Integer yd) {
        this.yd = yd;
    }

    public Integer getSf() {
        return sf;
    }

    public void setSf(Integer sf) {
        this.sf = sf;
    }

    public Integer getEms() {
        return ems;
    }

    public void setEms(Integer ems) {
        this.ems = ems;
    }

    public Integer getJd() {
        return jd;
    }

    public void setJd(Integer jd) {
        this.jd = jd;
    }

    public Integer getJdcod() {
        return jdcod;
    }

    public void setJdcod(Integer jdcod) {
        this.jdcod = jdcod;
    }
}