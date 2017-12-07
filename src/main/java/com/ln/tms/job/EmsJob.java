package com.ln.tms.job;

import com.ln.tms.sync.ems.EmsService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * EMSJob - EMSJob
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class EmsJob {

    @Autowired
    private EmsService emsService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 35 * * * ?")
    public void timingUpJob() {
        emsService.syncInfo();
        updateIndexUtils.updateOrAddIndex();
    }

}