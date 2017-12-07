package com.ln.tms.service;

import com.ln.tms.mapper.InfoTraceMapper;
import com.ln.tms.pojo.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * InfoTraceService - 快递跟踪轨迹信息Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class InfoTraceService {

    @Autowired
    private InfoTraceMapper infoTraceMapper;

    /**
     * 批量保存
     *
     * @param list List
     */
    @Transactional
    public void saveInfoTrace(List<Info> list) {
        infoTraceMapper.insertInfoTrace(list);
    }

    /**
     * 批量删除
     *
     * @param ids ID
     * @return Integer
     */
    @Transactional
    public Integer deleteByIds(List<Object> ids) {
        return infoTraceMapper.deleteByIds(ids);
    }
}