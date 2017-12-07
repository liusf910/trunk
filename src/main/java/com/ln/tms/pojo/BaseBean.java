package com.ln.tms.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * BaseBean - 基类
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = -404171753220605362L;
    /**
     * 创建日期
     */
    @JsonIgnore
    private Date createDate;

    /**
     * 修改日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyDate;

    /**
     * 当前页码
     */
    @JsonIgnore
    @Transient
    private Integer pageCurrent;

    /**
     * 显示条数
     */
    @JsonIgnore
    @Transient
    private Integer pageSize;

    /**
     * 开始查询的条数
     */
    @JsonIgnore
    @Transient
    private Integer begin;

    /**
     * 结束的条数
     */
    @JsonIgnore
    @Transient
    private Integer end;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getBegin() {
        return (pageCurrent - 1) * pageSize;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getEnd() {
        return pageSize;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(Integer pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}