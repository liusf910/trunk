package com.ln.tms.sync.jingdong;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.etms.EtmsTraceGetRequest;
import com.jd.open.api.sdk.response.etms.EtmsTraceGetResponse;
import com.jd.open.api.sdk.response.etms.TraceApiDto;
import com.ln.tms.sync.ICallable;

import java.util.List;


/**
 * JingDongCallableImpl - 京东回调接口实现
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class JingDongCallableImpl implements ICallable<List<TraceApiDto>> {

    private String logisticCode;

    private JdClient client;

    public JingDongCallableImpl(JdClient client, String logisticCode) {
        this.client = client;
        this.logisticCode = logisticCode;
    }

    @Override
    public List<TraceApiDto> call() {
        EtmsTraceGetRequest request = new EtmsTraceGetRequest();
        request.setWaybillCode(logisticCode);
        EtmsTraceGetResponse response;
        try {
            response = client.execute(request);
        } catch (JdException e) {
            throw new RuntimeException("京东跟踪信息doPost失败".concat(e.getMessage()));
        }
        if (response != null) {
            return response.getTraceApiDtos();
        }
        return null;
    }
}
