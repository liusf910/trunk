package com.ln.tms.job;

import com.ln.tms.service.CountRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TmsDataJob - 获取快递公司时效
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class TimeRateJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeRateJob.class);

    @Autowired
    private CountRateService countRateService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void tmsCalRate() {
        LOGGER.warn("获取INFO表数据快递时效Job=================>开始");
        countRateService.getTimeRate();
        LOGGER.warn("获取INFO表数据快递时效Job=================>结束");
    }


}