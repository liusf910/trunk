package com.ln.tms.bean;

/**
 * HttpResult - POST请求返回的结果
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class HttpResult {

    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应数据
     */
    private String data;

    public HttpResult() {

    }

    public HttpResult(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
