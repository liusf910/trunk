package com.ln.tms.job;

import com.ln.tms.sync.jingdong.JingDongService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * JingDongJob - 京东Job
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class JingDongJob {

    @Autowired
    private JingDongService jingDongService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 25 * * * ?")
    public void timingUpJob() {
        jingDongService.syncInfo();
        updateIndexUtils.updateOrAddIndex();
    }
}
