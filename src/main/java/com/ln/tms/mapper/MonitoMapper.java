package com.ln.tms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ln.tms.bean.InfoWhere;
import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.pojo.InfoMonito;

/**
 * MonitoMapper 后台管理监测Mapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface MonitoMapper extends MyMapper<InfoMonito> {
   
	/**
	 * 保存导出字段
	 * @param exportFields 导出字段Vo
	 */
	void saveExportFields(ExportFields exportFields);
    
	/**
	 * 检查所属模块查询是否存在
	 * @param exportFields 导出字段Vo
	 * @return  Integer 
	 */
	Integer checkExortFields(ExportFields exportFields);
   
	/**
	 * 修改导出字段
	 * @param exportFields 导出字段Vo
	 */
	void updExportFields(ExportFields exportFields);
   
	/**
	 * 根据所属模块查询导出字段
	 * @param exportFields  导出字段Vo
	 * @return ExportFields 导出字段Vo
	 */
	ExportFields getExportFieldsByBelongTo(ExportFields exportFields);
   
	/**
	 * 根据速递id查询详情信息
	 * @param infoId 	        速递id
	 * @return InfoMonito  速递信息Vo
	 */
	InfoMonito queryInfoByInfoId(String infoId);
   
	/**
	 * 根据查询条件查询info信息（不分页 （导出数据用））
	 * @param infoWhere 查询条件
	 * @return List<InfoMonito> 
	 */
	List<InfoMonito> queryInfoListNoPage(@Param("where") InfoWhere infoWhere);
  
	

  

}
