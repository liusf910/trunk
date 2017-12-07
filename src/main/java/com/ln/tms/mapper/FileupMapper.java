package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Fileup;

import java.util.List;

/**
 * FileupMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface FileupMapper extends MyMapper<Fileup> {

    /**
     * 多条件分页查询文件记录列表
     *
     * @param fileup
     * @return List
     */
    List<Fileup> queryFileupList(Fileup fileup);

    /**
     * 多条件查询文件记录的总条数
     *
     * @param fileup
     * @return Integer
     */
    Integer queryTotal(Fileup fileup);
}
