package com.ln.tms.sync.ems;

import com.ln.tms.bean.HttpResult;
import com.ln.tms.handler.HttpClientHandler;
import com.ln.tms.pojo.Courier;
import com.ln.tms.sync.ICallable;

import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * EmsCallableImpl - EMS回调接口实现
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class EmsCallableImpl implements ICallable<String> {

    private HttpClientHandler httpClientHandler;

    private Courier courier;

    private Map<String, String> maps;

    private Semaphore aSemaphore;

    public EmsCallableImpl(HttpClientHandler httpClientHandler, Courier courier, Map<String, String> maps, Semaphore aSemaphore) {
        this.httpClientHandler = httpClientHandler;
        this.courier = courier;
        this.maps = maps;
        this.aSemaphore = aSemaphore;
    }

    @Override
    public String call() {
        try {
            aSemaphore.acquire();
            HttpResult httpResult = httpClientHandler.doGet(courier.getApiUrl(), maps);
            aSemaphore.release();
            if (httpResult.getCode() == 200) {
                return httpResult.getData();
            }
        } catch (Exception e) {
            throw new RuntimeException(courier.getCourierName().concat("跟踪信息doPost失败"));
        }
        return null;
    }

}