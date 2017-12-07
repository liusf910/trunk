package com.ln.tms.job;

import com.ln.tms.sync.baishi.BaiShiService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * BaiShiJob - 百世汇通Job
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class BaiShiJob {

    @Autowired
    private BaiShiService baiShiService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 15 * * * ?")
    public void timingUpJob() {
        baiShiService.syncInfo();
        updateIndexUtils.updateOrAddIndex();

    }

}