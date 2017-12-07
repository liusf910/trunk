package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Log;

import java.util.List;

/**
 * FormulaMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface LogMapper extends MyMapper<Log> {

    /**
     * 多条件分页查询日志列表
     *
     * @param log
     * @return List
     */
    List<Log> queryLogAll(Log log);

    /**
     * 多条件查询日志的总条数
     *
     * @param log
     * @return Integer
     */
    Integer queryTotal(Log log);

    /**
     * 根据日志id查询日志信息
     *
     * @param logId
     * @return Log
     */
    Log queryByIdNew(Long logId);
}
