package com.ln.tms.job;

import com.ln.tms.erpsync.ErpSyncService;
import com.ln.tms.util.UpdateIndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ErpSyncJob - 获取ERP中间表数据Job
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class ErpSyncJob {

    @Autowired
    private ErpSyncService erpSyncService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;

    @Scheduled(cron = "0 52 * * * ?")
    public void tmsDataByErp() {
        erpSyncService.execute();
        updateIndexUtils.updateOrAddIndex();
    }


}