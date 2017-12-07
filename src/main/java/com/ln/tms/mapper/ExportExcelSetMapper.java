package com.ln.tms.mapper;

import org.apache.ibatis.annotations.Param;

import com.ln.tms.pojo.ExportFields;

/**
 * ExportExcelSetMapper 导出字段设置Mapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface ExportExcelSetMapper  {
    
	ExportFields getExportFieldsByBelongTo(@Param(value="belongTo") String belongTo);

	int checkExortFields(ExportFields exportFields);

	void updExportFields(ExportFields exportFields);

	void saveExportFields(ExportFields exportFields);
   
	

  

}
