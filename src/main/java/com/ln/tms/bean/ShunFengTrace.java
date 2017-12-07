package com.ln.tms.bean;

import java.util.List;

public class ShunFengTrace {

    private boolean success = true;

    private String errCode;

    private String errMsg;

    private List<List<Trace>> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<List<Trace>> getResult() {
        return result;
    }

    public void setResult(List<List<Trace>> result) {
        this.result = result;
    }


}
