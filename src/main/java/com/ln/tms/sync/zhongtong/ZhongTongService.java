package com.ln.tms.sync.zhongtong;

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
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * ZhongTongService - 中通快递对接Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class ZhongTongService implements SyncInfoInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhongTongService.class);
    private static final String ZT = "";
    private static final String shipperCode = "shipperCode";
    private static final String state = "state";
    @Autowired
    private InfoService infoService;

    @Autowired
    private CourierService courierService;

    @Autowired
    private HttpClientHandler httpClientHandler;

    @Override
    public void syncInfo() {
        Courier courier = courierService.queryOne(new Courier(ZT));
        Example example = new Example(Info.class);
        example.createCriteria().andNotEqualTo(state, 3).andEqualTo(shipperCode, ZT);
        List<Info> list = infoService.queryByExample(example);
        LOGGER.info("非已签收状态的中通快递条数={}", list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            long start = System.currentTimeMillis();
            ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(80));
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            ConcurrentLinkedQueue<Info> infoQueue = new ConcurrentLinkedQueue<>();
            for(Info info : list){
                Map<String, String> maps = null;
                ListenableFuture<String> listenableFuture = listeningExecutorService.submit(new CallableImpl(httpClientHandler, courier, maps));
                Futures.addCallback(listenableFuture, new ZhongTongIFutureCallback(infoService, countDownLatch,infoQueue));
            }
            try{
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
                LOGGER.info("中通快递跟踪信息同步数=====>{},同步耗时====>{}", infos.size(), (end - start));
            }catch (InterruptedException e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
