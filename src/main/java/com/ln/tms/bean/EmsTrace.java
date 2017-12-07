package com.ln.tms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * EmsTrace - ems轨迹信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmsTrace {

    private List<Traces> traces;

    public void setTraces(List<Traces> traces) {
        this.traces = traces;
    }

    public List<Traces> getTraces() {
        return this.traces;
    }

    public static class Traces {

        private String acceptTime; // 站点操作时间

        private String acceptAddress; // 站点地址

        private String remark; // 备注

        public String getAcceptTime() {
            return acceptTime;
        }

        public void setAcceptTime(String acceptTime) {
            this.acceptTime = acceptTime;
        }

        public String getAcceptAddress() {
            return acceptAddress;
        }

        public void setAcceptAddress(String acceptAddress) {
            this.acceptAddress = acceptAddress;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

    }
}