package com.ln.tms.sync;

import com.ln.tms.bean.HttpResult;
import com.ln.tms.handler.HttpClientHandler;
import com.ln.tms.pojo.Courier;

import java.io.IOException;
import java.util.Map;

/**
 * CallableImpl - 回调接口实现
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class CallableImpl implements ICallable<String> {

    private HttpClientHandler httpClientHandler;

    private Courier courier;

    private Map<String, String> maps;

    public CallableImpl(HttpClientHandler httpClientHandler, Courier courier, Map<String, String> maps) {
        this.httpClientHandler = httpClientHandler;
        this.courier = courier;
        this.maps = maps;
    }

    @Override
    public String call() {
        try {
            HttpResult httpResult = httpClientHandler.doPost(courier.getApiUrl(), maps);
            if (httpResult.getCode() == 200) {
                return httpResult.getData();
            }
        } catch (IOException e) {
            throw new RuntimeException(courier.getCourierName().concat("跟踪信息doPost失败").concat(e.getMessage()));
        }
        return null;

    }
}