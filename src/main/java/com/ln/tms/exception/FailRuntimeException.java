package com.ln.tms.exception;

/**
 * FailRuntimeException
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class FailRuntimeException extends RuntimeException {

    private String retCd;  //异常对应的返回码
    private String msgDes;  //异常对应的描述信息

    public FailRuntimeException() {
        super();
    }

    public FailRuntimeException(String message) {
        super(message);
        msgDes = message;
    }

    public FailRuntimeException(String retCd, String msgDes) {
        super();
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return retCd;
    }

    public void setRetCd(String retCd) {
        this.retCd = retCd;
    }

    public String getMsgDes() {
        return msgDes;
    }

    public void setMsgDes(String msgDes) {
        this.msgDes = msgDes;
    }
}
