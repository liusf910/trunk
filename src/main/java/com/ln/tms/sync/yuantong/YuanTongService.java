package com.ln.tms.sync.yuantong;

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
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * YuanTongService - 圆通快递对接Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class YuanTongService implements SyncInfoInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(YuanTongService.class);
    private static final String yuantong = "10097";
    private static final String shipperCode = "shipperCode";
    private static final String state = "state";

    @Autowired
    private HttpClientHandler httpClientHandler;

    @Autowired
    private InfoService infoService;

    @Autowired
    private CourierService courierService;

    @Override
    public void syncInfo() {
        Courier courier = courierService.queryOne(new Courier(yuantong));
        Example example = new Example(Info.class);
        example.createCriteria().andNotEqualTo(state, 3).andEqualTo(shipperCode, yuantong);
        List<Info> list = infoService.queryByExample(example);
        LOGGER.info("非已签收状态的圆通快递条数={}", list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            long start = System.currentTimeMillis();
            ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(80));
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            ConcurrentLinkedQueue<Info> infoQueue = new ConcurrentLinkedQueue<>();
            for (Info info : list) {
                Map<String, String> maps = processingParameters(info.getLogisticCode(), courier);
                ListenableFuture<String> listenableFuture = listeningExecutorService.submit(new CallableImpl(httpClientHandler, courier, maps));
                Futures.addCallback(listenableFuture, new YuanTongIFutureCallback(countDownLatch, infoQueue, info));
            }
            try {
                countDownLatch.await();
                listeningExecutorService.shutdown();
                List<Info> infos = new ArrayList<>();
                while (!infoQueue.isEmpty()) {
                    Info info = infoQueue.poll();
                    infos.add(info);
                }
                if (CollectionUtils.isNotEmpty(infos)) {
                    List<List<Info>> subList = TmsUtils.getSubList(infos, 1000);
                    for (List<Info> infoList : subList) {
                        infoService.updateListInfo(infoList);
                    }
                }
                long end = System.currentTimeMillis();
                LOGGER.info("圆通快递跟踪信息同步数=====>{},同步耗时====>{}", infos.size(), (end - start));
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
        String appKey = courier.getAppKey();
        String method = courier.getMethod();
        String userId = courier.getUserId();
        String apiVersion = courier.getApiVersion();
        String format = courier.getFormat();
        DateTime dateTime = new DateTime(new Date());
        String timestamp = dateTime.toString(DateUtils.DATETIMEFORMAT);
        StringBuffer sb = new StringBuffer();
        sb.append(secretKey);
        sb.append("app_key");
        sb.append(appKey);
        sb.append("format");
        sb.append(format);
        sb.append("method");
        sb.append(method);
        sb.append("timestamp");
        sb.append(timestamp);
        sb.append("user_id");
        sb.append(userId);
        sb.append("v");
        sb.append(apiVersion);
        Map<String, String> map = new HashMap<>();
        map.put("sign", StringUtils.swapCase(DigestUtils.md5Hex(sb.toString())));
        map.put("app_key", appKey);
        map.put("format", format);
        map.put("method", method);
        map.put("timestamp", timestamp);
        map.put("method", method);
        map.put("user_id", userId);
        map.put("v", apiVersion);
        map.put("param", "[{'Number':'" + logisticCode + "'}]");
        return map;
    }


}