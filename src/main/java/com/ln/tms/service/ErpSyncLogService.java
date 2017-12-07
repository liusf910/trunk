package com.ln.tms.service;

import com.ln.tms.pojo.ErpSyncLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ErpLogService - 获取ERP中间表数据日志Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class ErpSyncLogService extends BaseService<ErpSyncLog> {

    @Override
    @Transactional
    public Integer save(ErpSyncLog erpSyncLog) {
        return super.save(erpSyncLog);
    }
}