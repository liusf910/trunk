package com.ln.tms.sync.ems;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ln.tms.handler.HttpClientHandler;
import com.ln.tms.pojo.Courier;
import com.ln.tms.pojo.Info;
import com.ln.tms.service.CourierService;
import com.ln.tms.service.InfoService;
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
import java.util.concurrent.Semaphore;

/**
 * EMSService - EMS快递对接Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpellCheckingInspection")
@Service
public class EmsService implements SyncInfoInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmsService.class);
    private static final String EMS = "10093";
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
        Courier courier = courierService.queryOne(new Courier(EMS));
        Example example = new Example(Info.class);
        example.createCriteria().andNotEqualTo(STATE, 3).andEqualTo(SHIPPERCODE, EMS);
        List<Info> list = infoService.queryByExample(example);
        LOGGER.warn("非已签收状态的EMS快递 条数={}", list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            long start = System.currentTimeMillis();
            //使用Future模式
            ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(80));
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            ConcurrentLinkedQueue<Info> infoQueue = new ConcurrentLinkedQueue<>();
            final Semaphore aSemaphore = new Semaphore(5);//信号量，用于控制TPS限制
            try {
                for (Info info : list) {
                    //快递单号
                    String logisticCodes = info.getLogisticCode();
                    Map<String, String> maps = processingParameters(logisticCodes, courier);
                    ListenableFuture<String> listenableFuture = listeningExecutorService.submit(new EmsCallableImpl(httpClientHandler, courier, maps, aSemaphore));
                    Futures.addCallback(listenableFuture, new EmsIFutureCallback(info, countDownLatch, infoQueue));
                }
                countDownLatch.await();
                listeningExecutorService.shutdown();
                List<Info> infos = new ArrayList<>();
                while (!infoQueue.isEmpty()) {
                    Info info = infoQueue.poll();
                    infos.add(info);
                }
                if (CollectionUtils.isNotEmpty(infos)) {
                    List<List<Info>> subList = TmsUtils.getSubList(infos, 300);
                    for (List<Info> infoList : subList) {
                        infoService.updateListInfo(infoList);
                    }
                }
                long end = System.currentTimeMillis();
                LOGGER.warn("EMS快递跟踪信息同步数=====>" + list.size() + "同步耗时====>" + (end - start));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }

    }

    /**
     * GET参数
     *
     * @param logisticCode String
     * @param courier      Courier
     * @return Map
     */
    private Map<String, String> processingParameters(String logisticCode, Courier courier) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("version", courier.getApiVersion());
        map.put("authenticate", courier.getAppKey());
        map.put("logisticCode", logisticCode);
        return map;
    }


}