package com.ln.tms.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InfoTrace - 快递跟踪信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_info_trace")
public class InfoTrace {

    /**
     * 物流信息ID
     */
    @Id
    private Long infoId;

    /**
     * 轨迹信息json格式
     */
    private String infoTrace;

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public String getInfoTrace() {
        return infoTrace;
    }

    public void setInfoTrace(String infoTrace) {
        this.infoTrace = infoTrace;
    }

}