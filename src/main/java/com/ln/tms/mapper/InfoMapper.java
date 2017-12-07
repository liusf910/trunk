package com.ln.tms.mapper;

import com.github.pagehelper.PageInfo;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.UserStorage;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * InfoMapper - 快递基本信息mapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface InfoMapper extends MyMapper<Info> {

    /**
     * 批量保存快递基本信息
     *
     * @param list List
     */
    void saveInfoList(List<Info> list);

    /**
     * 条件查询快递信息
     *
     * @param where 条件
     * @return List
     */
    PageInfo<Info> selectInfoByWhere(@Param("where") InfoWhere where);

    /**
     * 批量更新快递信息和轨迹信息
     *
     * @param list List
     */
    void updateListInfo(List<Info> list);
    
    /**
     * 根据userId查询对应得仓库和快递公司
     * @param userId
     * @return
     */
	UserStorage getUserStorageCourier(@Param("userId") String userId);

	/**查询Info的数据条数
	 * @return
	 */
	Integer queryInfoAmount();

}
