package com.ln.tms.service;

import com.ln.tms.enums.JobType;
import com.ln.tms.erpsync.ErpSyncService;
import com.ln.tms.pojo.Log;
import com.ln.tms.pojo.User;
import com.ln.tms.sync.baishi.BaiShiService;
import com.ln.tms.sync.ems.EmsService;
import com.ln.tms.sync.jingdong.JingDongService;
import com.ln.tms.sync.shentong.ShenTongService;
import com.ln.tms.sync.yuantong.YuanTongService;
import com.ln.tms.util.UpdateIndexUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * InfoTsService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class TriggerService {

    @Autowired
    private LogService logService;

    @Autowired
    private CountRateService countRateService;

    @Autowired
    private ErpSyncService erpSyncService;

    @Autowired
    private BaiShiService baiShiService;

    @Autowired
    private EmsService emsService;

    @Autowired
    private JingDongService jingDongService;

    @Autowired
    private ShenTongService shenTongService;

    @Autowired
    private YuanTongService yuanTongService;

    @Autowired
    private UpdateIndexUtils updateIndexUtils;


    /**
     * 手动触发自动任务
     *
     * @param jobType 任务类型
     * @param date    选择统计日期
     * @param user    操作用户
     */
    public void chooseJob(JobType jobType, String date, User user) {
        final JobType type = jobType;
        final String dt = date;
        final User us = user;
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer suf = new StringBuffer();
                try {
                    switch (type.getCode()) {
                        case 1:
                            countRateService.getTimeRate(dt);
                            break;
                        case 2:
                            erpSyncService.execute();
                            break;
                        case 3:
                            baiShiService.syncInfo();
                            break;
                        case 4:
                            emsService.syncInfo();
                            break;
                        case 5:
                            jingDongService.syncInfo();
                            break;
                        case 6:
                            shenTongService.syncInfo();
                            break;
                        case 7:
                            yuanTongService.syncInfo();
                            break;
                    }
                    updateIndexUtils.updateOrAddIndex();
                    suf.append("任务执行成功!");
                } catch (Exception e) {
                    suf.append("任务执行失败!");
                    e.printStackTrace();
                } finally {
                    Log log = new Log();
                    log.setOperation(type.getDesc());
                    log.setUserId(us.getUserId());
                    log.setIp(us.getIp());
                    if (StringUtils.isNotBlank(dt)) {
                        log.setParameter(dt);
                    }
                    log.setContent(suf.toString());
                    logService.save(log);
                }
            }
        }).start();
    }
}
