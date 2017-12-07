package com.ln.tms.sync.shunfeng;

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
 * ShunFengService - 顺丰快递对对接Service
 *
 * @author zhengyu
 */
@Service
public class ShunFengService implements SyncInfoInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShunFengService.class);
    private static final String SHUNFENG = "10091";
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
        //获取顺丰店铺配置
        Courier courier = courierService.queryOne(new Courier(SHUNFENG));
        Example example = new Example(Info.class);
        List<String> storageCode = new ArrayList<String>();
        storageCode.add("9021");
        storageCode.add("9026");
        storageCode.add("9099");
        example.createCriteria().andNotEqualTo(STATE, 3).andEqualTo(SHIPPERCODE, SHUNFENG).andIn("storageCode", storageCode);
        //查询顺丰下面所有非已签收状态的订单
        List<Info> list = infoService.queryByExample(example);
        LOGGER.warn("非已签收状态的顺丰快递 条数={}", list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            //每次查询十个单号
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
                Futures.addCallback(listenableFuture, new ShunFengIFutureCallback(infoService, countDownLatch));
            }
            try {
                countDownLatch.await();
                listeningExecutorService.shutdown();
                long end = System.currentTimeMillis();
                LOGGER.warn("顺丰快递跟踪信息同步数=====>" + list.size() + "同步耗时====>" + (end - start));
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
        String checkCode = courier.getAppKey();
        String checkWord = courier.getSecretKey();
        String xml = getXml(logisticCode, checkCode);
        Map<String, String> map = new HashMap<>();
        map.put("xml", xml);
        map.put("verifyCode", getDigest(xml + checkWord, "utf-8"));
        return map;
    }

    /**
     * 获取要传递的xml报文
     *
     * @param logisticCode 单号
     * @param checkCode    校检码
     * @return String格式的xml报文
     */
    private String getXml(String logisticCode, String checkCode) {
        return "<?xml version='1.0' encoding='UTF-8'?>"
                + "<Request service='RouteService' lang='zh-CN'>"
                + "<Head>" + checkCode + "</Head>"
                + "<Body>"
                + "<RouteRequest tracking_number='" + logisticCode + "'>"
                + "</RouteRequest>"
                + "</Body>"
                + "</Request>";
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
