package com.ln.tms.service;

import com.ln.tms.mapper.LogMapper;
import com.ln.tms.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * LogService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class LogService extends BaseService<Log> {

    @Autowired
    private LogMapper logMapper;


    /**
     * 条件查询日志
     *
     * @param log 查询条件
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Log> queryLogList(Log log) {
        return logMapper.queryLogAll(log);
    }

    /**
     * 条件查询日志集合
     *
     * @param log 查询条件
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer queryTotal(Log log) {
        return logMapper.queryTotal(log);
    }

    /**
     * 根据日志id查询日志信息
     *
     * @param logId 日志编号
     * @return Log
     */
    @Transactional(readOnly = true)
    public Log queryByIdNew(Long logId) {
        return logMapper.queryByIdNew(logId);
    }

    /**
     * 批量删除日志
     *
     * @param ids 日志编号集合
     */
    @Transactional
    public void deleteBatch(List<Object> ids) {
        super.deleteByIds(ids, "id", Log.class);
    }

    @Override
    @Transactional
    public Integer saveSelective(Log log) {
        return super.saveSelective(log);
    }
}