package com.ln.tms.sync.baishi;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ln.tms.handler.HttpClientHandler;
import com.ln.tms.pojo.Courier;
import com.ln.tms.pojo.Info;
import com.ln.tms.service.CourierService;
import com.ln.tms.service.InfoService;
import com.ln.tms.sync.CallableImpl;
import com.ln.tms.sync.SyncInfoInterface;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * BaiShiService - 百世汇通快递对接Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpellCheckingInspection")
@Service
public class BaiShiService implements SyncInfoInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaiShiService.class);
    private static final String BAISHI = "12402";
    private static final String SHIPPERCODE = "shipperCode";
    private static final String STATE = "state";

    @Autowired
    private HttpClientHandler httpClientHandler;

    @Autowired
    private InfoService infoService;

    @Autowired
    private CourierService courierService;

    @Override
    public void syncInfo() {
        Courier courier = courierService.queryOne(new Courier(BAISHI));
        Example example = new Example(Info.class);
        example.createCriteria().andNotEqualTo(STATE, 3).andEqualTo(SHIPPERCODE, BAISHI);
        List<Info> list = infoService.queryByExample(example);
        LOGGER.warn("非已签收状态的百世汇通快递 条数={}", list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            List<List<Info>> subList = TmsUtils.getSubList(list, 10);
            LOGGER.warn("根据快递公司接口要求每次只能传递10个快递单号，获取拆分后的subList ,大小={}", subList.size());
            long start = System.currentTimeMillis();
            ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(80));
            CountDownLatch countDownLatch = new CountDownLatch(subList.size());
            for (List<Info> infos : subList) {
                final List<String> logisticCodes = new ArrayList<>();
                for (Info info : infos) {
                    logisticCodes.add(info.getLogisticCode());
                }
                Map<String, String> maps = processingParameters(StringUtils.join(logisticCodes, ","), courier);
                ListenableFuture<String> listenableFuture = listeningExecutorService.submit(new CallableImpl(httpClientHandler, courier, maps));
                Futures.addCallback(listenableFuture, new BaiShiIFutureCallback(infoService, countDownLatch));
            }
            try {
                countDownLatch.await();
                listeningExecutorService.shutdown();
                long end = System.currentTimeMillis();
                LOGGER.warn("百世汇通快递跟踪信息同步数=====>" + list.size() + "同步耗时====>" + (end - start));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }

    }

    /**
     * POST参数
     *
     * @param logisticCode String
     * @param courier      Courier
     * @return Map
     */
    private Map<String, String> processingParameters(String logisticCode, Courier courier) {
        String secretKey = courier.getSecretKey();
        String method = courier.getMethod();
        String userId = courier.getUserId();
        Map<String, String> map = new HashMap<>();
        String digest = this.getDigest(logisticCode + secretKey, "UTF-8");
        map.put("parternID", userId);
        map.put("mailNo", logisticCode);
        map.put("serviceType", method);
        map.put("digest", digest);//签名字符串
        return map;
    }

    /**
     * 签名字符串处理
     *
     * @param digestString String
     * @param encode       String
     * @return String
     */
    private String getDigest(String digestString, String encode) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(digestString.getBytes(encode));
            byte[] b = md.digest();
            return (new sun.misc.BASE64Encoder()).encode(b);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            LOGGER.error("签名失败");
        }
        return null;
    }

}