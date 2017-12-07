package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.InfoTsTemp;

import java.util.List;

/**
 * InfoTsTempMapper - 快递导入临时表信息mapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface InfoTsTempMapper extends MyMapper<InfoTsTemp> {
    /**
     * 批量新增(揽件签收)临时表
     *
     * @param infoTss
     * @return Integer
     */
    Integer saveInfoTsTempBatch(List<InfoTsTemp> infoTss);

    /**
     * 清除临时表
     *
     * @return Integer
     */
    Integer clearInfoTsTemp();

    /**
     * 批量新增(揽件签收)快递信息表
     *
     * @return Integer
     */
    Integer saveBatch();


    /**
     * 揽件批量更新快递信息表
     *
     * @return Integer
     */
    Integer updateTookBatch();

    /**
     * 签收批量更新快递信息表
     *
     * @return Integer
     */
    Integer updateSignBatch();

    /**
     * 批量更新导入揽签数据的快递编号
     *
     * @return Integer
     */
    Integer updateShipperCode();

    /**
     * 查询揽签导出的数据
     *
     * @return List
     */
    List<InfoTsTemp> queryInfos();

    /**
     * 更新临时表
     *
     * @param infoTsTemp 更新数据
     * @return Integer
     */
    Integer updateTsTemp(InfoTsTemp infoTsTemp);

    /**
     * 更新临时表已对接的揽收数据
     *
     * @return Integer
     */
    Integer updateIgnoreTook();


    /**
     * 更新临时表揽签重复数据忽略
     *
     * @return
     */
    Integer updateIgnoreRepeat();

    /**
     * 锁表
     */
    void lockTable();

    /**
     * 解锁
     */
    void unlockTable();
    
    /**
     * 修改tms_info表中的状态(1:已揽收)
     */
	void updTookInfoState();
    
	/**
	 * 获取要导入的数据
	 * @return
	 */
	List<Info> getTsTempsInfo();
    
	/**
	 * 修改tms_info表中的状态和签收超期天数(3:已签收)
	 * @param info
	 */
	void updSignInfoState(Info info);
}
