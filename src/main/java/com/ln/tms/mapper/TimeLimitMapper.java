package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.TimeLimit;

import java.util.List;

/**
 * TimeLimitMapper - 快递公司时限mapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface TimeLimitMapper extends MyMapper<TimeLimit> {

    /**
     * 批量添加时效
     *
     * @param limitList
     * @return Integer
     */
    Integer saveTimeLimitBatch(List<TimeLimit> limitList);

    /**
     * 清空时效表
     */
    void clearTimeLimit();

    /**
     * 获取所有的省份
     *
     * @return List
     */
    List<String> queryProvince();
}
