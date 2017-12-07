package com.ln.tms.mymapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * MyMapper - 定义自己的Mapper该接口不能被扫描到
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
