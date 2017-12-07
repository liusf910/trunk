package com.ln.tms.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ErpSyncLog - 获取ERP中间表数据日志
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_erp_sync_log")
public class ErpSyncLog extends BaseBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * jdbc删除条数
     */
    private Integer dSize;

    /**
     * 已存在本地数据库中的条数
     */
    private Integer rSize;

    /**
     * 获取数据条数
     */
    private Integer size;

    /**
     * 获取耗时
     */
    private Long timeConsum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getdSize() {
        return dSize;
    }

    public void setdSize(Integer dSize) {
        this.dSize = dSize;
    }

    public Integer getrSize() {
        return rSize;
    }

    public void setrSize(Integer rSize) {
        this.rSize = rSize;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTimeConsum() {
        return timeConsum;
    }

    public void setTimeConsum(Long timeConsum) {
        this.timeConsum = timeConsum;
    }
}