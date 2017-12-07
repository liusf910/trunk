package com.ln.tms.job;

import com.ln.tms.sync.shunfeng.ShunFengService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ShunFengJob - 顺丰job
 *
 * @author zhengyu
 */
@Component
public class ShunFengJob {

    @Autowired
    private ShunFengService shunFengService;
	
    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 10 * * * ?")
    public void timingUpJob() {
        shunFengService.syncInfo();
        updateIndexUtils.updateOrAddIndex();
    }
}
