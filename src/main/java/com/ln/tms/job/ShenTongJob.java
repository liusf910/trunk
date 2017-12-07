package com.ln.tms.job;

import com.ln.tms.sync.shentong.ShenTongService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * YuanTongJob - 申通快递Job
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class ShenTongJob {

    @Autowired(required = false)
    private ShenTongService shenTongService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 45 * * * ?")
    public void timingUpJob() {
        shenTongService.syncInfo();
        updateIndexUtils.updateOrAddIndex();
    }
}
