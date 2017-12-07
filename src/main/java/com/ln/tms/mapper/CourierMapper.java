package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Courier;

import java.util.List;

/**
 * CourierMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface CourierMapper extends MyMapper<Courier> {

    /**
     * 校验快递公司名称
     *
     * @param courier
     * @return Integer
     */
    Integer checkCourierName(Courier courier);

    /**
     * 根据Id查询快递公司信息
     *
     * @param shipperCode
     * @return Courier
     */
    Courier getCourierById(String shipperCode);

    /**
     * 判断快递公司是否关联了仓库
     *
     * @param shipperCode
     * @return Integer
     */
    Integer getBindStorageCourierNum(String shipperCode);

    /**
     * 查询所有快递
     *
     * @return List
     */
    List<Courier> queryAllNew();
}
