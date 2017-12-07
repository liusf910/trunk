package com.ln.tms.sync.jingdong;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.response.etms.TraceApiDto;
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
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * JingDongService - 京东快递对接Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class JingDongService implements SyncInfoInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(JingDongService.class);
    private static final String JD = "13344";
    private static final String JDCOD = "13343";
    private static final String shipperCode = "shipperCode";
    private static final String state = "state";


    @Autowired
    private InfoService infoService;

    @Autowired
    private CourierService courierService;

    @Override
    public void syncInfo() {
        Courier courier = courierService.queryOne(new Courier(JD));
        JdClient client = new DefaultJdClient(courier.getApiUrl(), courier.getMethod(), courier.getAppKey(), courier.getSecretKey());
        Example example = new Example(Info.class);
        example.createCriteria().andNotEqualTo(state, 3).andEqualTo(shipperCode, JD);
        List<Info> list = infoService.queryByExample(example);
        LOGGER.info("非已签收状态的京东快递条数={}", list.size());
        Example exampleCod = new Example(Info.class);
        exampleCod.createCriteria().andNotEqualTo(state, 3).andEqualTo(shipperCode, JDCOD);
        List<Info> listCod = infoService.queryByExample(exampleCod);
        LOGGER.info("非已签收状态的京东COD快递条数={}", listCod.size());
        list.addAll(listCod);
        if (CollectionUtils.isNotEmpty(list)) {
            long start = System.currentTimeMillis();
            ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(80));
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            ConcurrentLinkedQueue<Info> infoQueue = new ConcurrentLinkedQueue<>();
            for (Info info : list) {
                ListenableFuture<List<TraceApiDto>> listenableFuture = listeningExecutorService.submit(new JingDongCallableImpl(client, info.getLogisticCode()));
                Futures.addCallback(listenableFuture, new JingDongIFutureCallback(countDownLatch, infoQueue, info));
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
                LOGGER.warn("京东快递跟踪信息同步数=====>{},同步耗时====>{}", list.size(), (end - start));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

}
