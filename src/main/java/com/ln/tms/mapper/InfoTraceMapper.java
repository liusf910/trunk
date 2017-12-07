package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.InfoTrace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * InfoTraceMapper - 快递跟踪轨迹信息mapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface InfoTraceMapper extends MyMapper<InfoTrace> {

    /**
     * 批量保存
     *
     * @param list List
     */
    void insertInfoTrace(@Param("list") List<Info> list);

    /**
     * 批量删除
     *
     * @param ids ID
     * @return Integer
     */
    Integer deleteByIds(List<Object> ids);
}