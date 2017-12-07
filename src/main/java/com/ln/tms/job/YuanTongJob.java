package com.ln.tms.job;

import com.ln.tms.sync.yuantong.YuanTongService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * YuanTongJob - 圆通Job
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class YuanTongJob {

    @Autowired
    private YuanTongService yuanTongService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 5 * * * ?")
    public void timingUpJob() {
        yuanTongService.syncInfo();
        updateIndexUtils.updateOrAddIndex();
    }
}