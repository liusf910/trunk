package com.ln.tms.service;

import com.ln.tms.pojo.EmailConf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EmailConfService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class EmailConfService extends BaseService<EmailConf> {

    /**
     * 新增或保存邮件设置
     *
     * @param emailConf 数据
     * @return Integer
     */
    @Transactional
    public Integer saveOrUpdate(EmailConf emailConf) {
        if (emailConf.getEmailId() != null) {
            return super.updateSelective(emailConf);
        } else {
            return super.saveSelective(emailConf);
        }
    }
}
