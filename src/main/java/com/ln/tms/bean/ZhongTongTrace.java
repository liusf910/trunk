package com.ln.tms.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ZhongTongTrace 中通物流信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class ZhongTongTrace {
    private List<ZhongTongData> data;
    private String msg;
    private boolean status;

    public List<ZhongTongData> getData() {
        return data;
    }

    public void setData(List<ZhongTongData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class ZhongTongData{
        private String billcode;
        private List<Traces> traces;

        public String getBillcode() {
            return billcode;
        }

        public void setBillcode(String billcode) {
            this.billcode = billcode;
        }

        public List<Traces> getTraces() {
            return traces;
        }

        public void setTraces(List<Traces> traces) {
            this.traces = traces;
        }
        public static class Traces{
            private String desc;
            @JsonProperty("dispOrRecMan")
            private String disporrecman;
            @JsonProperty("dispOrRecManCode")
            private String disporrecmancode;
            @JsonProperty("dispOrRecManPhone")
            private String disporrecmanphone;
            @JsonProperty("isCenter")
            private String iscenter;
            @JsonProperty("preOrNextSite")
            private String preornextsite;
            @JsonProperty("preOrNextSiteCode")
            private String preornextsitecode;
            @JsonProperty("preOrNextSitePhone")
            private String preornextsitephone;
            private String remark;
            @JsonProperty("scanDate")
            private String scandate;
            @JsonProperty("scanSite")
            private String scansite;
            @JsonProperty("scanSiteCode")
            private String scansitecode;
            @JsonProperty("scanSitePhone")
            private String scansitephone;
            @JsonProperty("scanType")
            private String scantype;
            @JsonProperty("signMan")
            private String signman;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDisporrecman() {
                return disporrecman;
            }

            public void setDisporrecman(String disporrecman) {
                this.disporrecman = disporrecman;
            }

            public String getDisporrecmancode() {
                return disporrecmancode;
            }

            public void setDisporrecmancode(String disporrecmancode) {
                this.disporrecmancode = disporrecmancode;
            }

            public String getDisporrecmanphone() {
                return disporrecmanphone;
            }

            public void setDisporrecmanphone(String disporrecmanphone) {
                this.disporrecmanphone = disporrecmanphone;
            }

            public String getIscenter() {
                return iscenter;
            }

            public void setIscenter(String iscenter) {
                this.iscenter = iscenter;
            }

            public String getPreornextsite() {
                return preornextsite;
            }

            public void setPreornextsite(String preornextsite) {
                this.preornextsite = preornextsite;
            }

            public String getPreornextsitecode() {
                return preornextsitecode;
            }

            public void setPreornextsitecode(String preornextsitecode) {
                this.preornextsitecode = preornextsitecode;
            }

            public String getPreornextsitephone() {
                return preornextsitephone;
            }

            public void setPreornextsitephone(String preornextsitephone) {
                this.preornextsitephone = preornextsitephone;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getScandate() {
                return scandate;
            }

            public void setScandate(String scandate) {
                this.scandate = scandate;
            }

            public String getScansite() {
                return scansite;
            }

            public void setScansite(String scansite) {
                this.scansite = scansite;
            }

            public String getScansitecode() {
                return scansitecode;
            }

            public void setScansitecode(String scansitecode) {
                this.scansitecode = scansitecode;
            }

            public String getScansitephone() {
                return scansitephone;
            }

            public void setScansitephone(String scansitephone) {
                this.scansitephone = scansitephone;
            }

            public String getScantype() {
                return scantype;
            }

            public void setScantype(String scantype) {
                this.scantype = scantype;
            }

            public String getSignman() {
                return signman;
            }

            public void setSignman(String signman) {
                this.signman = signman;
            }
        }
    }

}
