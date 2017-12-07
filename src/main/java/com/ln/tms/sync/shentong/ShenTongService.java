package com.ln.tms.sync.shentong;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * ShenTongService - 申通快递对接Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class ShenTongService implements SyncInfoInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShenTongService.class);
    private static final String shentong = "10090";
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
        Courier courier = courierService.queryOne(new Courier(shentong));
        Example example = new Example(Info.class);
        example.createCriteria().andNotEqualTo(state, 3).andEqualTo(shipperCode, shentong);
        List<Info> list = infoService.queryByExample(example);
        LOGGER.warn("非已签收状态的申通快递条数={}", list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            LOGGER.warn("根据快递公司接口要求每次只能传递1个快递单号，获取拆分后的subList ,大小={}", 1);
            long start = System.currentTimeMillis();
            ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(80));
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            ConcurrentLinkedQueue<Info> infoQueue = new ConcurrentLinkedQueue<>();
            for (Info info : list) {
                String logisticCode = info.getLogisticCode();
                Map<String, String> maps = processingParameters(logisticCode, courier);
                ListenableFuture<String> listenableFuture = listeningExecutorService.submit(new CallableImpl(httpClientHandler, courier, maps));
                Futures.addCallback(listenableFuture, new ShenTongIFutureCallback(countDownLatch, infoQueue, info));
            }
            try {
                countDownLatch.await();
                listeningExecutorService.shutdown();
                List<Info> infos = new ArrayList<>();
                while (!infoQueue.isEmpty()) {
                    Info info = infoQueue.poll();
                    infos.add(info);
                }
                List<List<Info>> subList = new ArrayList<List<Info>>();
                if (infos.size() > 0) {
                    subList = TmsUtils.getSubList(infos, 1000);
                }
                for (List<Info> infoList : subList) {
                    infoService.updateListInfo(infoList);
                }
                long end = System.currentTimeMillis();
                LOGGER.warn("申通快递跟踪信息同步数=====>{},同步耗时====>{}", list.size(), (end - start));
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
        String sort = courier.getMethod();
        String format = courier.getFormat();
        Map<String, String> map = new HashMap<>();
        map.put("billcode", logisticCode);
        map.put("format", format);
        map.put("sort", sort);
        map.put("AppKey", appKey);
        map.put("AppSecret", secretKey);
        return map;
    }

}
