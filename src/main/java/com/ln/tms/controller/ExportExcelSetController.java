package com.ln.tms.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.service.ExportExcelSetService;

/**
 * ExportExcelSetController 导出字段设置
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/exportExcel")
public class ExportExcelSetController extends BaseController {
    
	/**
	 * 导出字段设置Service
	 */
	@Autowired
	private ExportExcelSetService exportExcelSetService;
	
	
	/**
	 * 导出字段设置页面
	 * @param belongTo  all：后台   courier:前台
	 * @return ModelAndView  导出字段选择页面
	 */
	@RequestMapping(value = "exportExcelSet")
	@ResponseBody
	public ModelAndView exportExcelSet(@RequestParam(value="belongToStr",required=false) String belongToStr) {
		if(StringUtils.isBlank(belongToStr)){
			belongToStr="all";
		}
		ModelAndView mv = new ModelAndView("/setting/exportexcel/exportExcelSetAll");
		ExportFields retExportFields = exportExcelSetService.getExportFieldsByBelongTo(belongToStr);
		mv.addObject("returnMap", exportExcelSetService.getReturnMap(retExportFields));
		mv.addObject("belongTo", belongToStr);
		return mv;
	}
    
	/**
	 * 保存导出字段
	 * @param exportFields 导出字段Vo
	 * @return Map
	 */
	@RequestMapping(value = "saveExportExcelSet", method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object> exportExcel(ExportFields exportFields) {
		ExecuteResult result = new ExecuteResult();
		//检查所属模块查询是否存在，若存在则修改，若不存在则新增.
    	boolean flag=exportExcelSetService.checkExortFields(exportFields);
    	if(flag){//修改
    		exportExcelSetService.updExportFields(exportFields);
    	}else{//新增
    		exportExcelSetService.saveExportFields(exportFields);
    	}
    	return result.jsonReturn(HttpStatus.SC_OK,"保存成功", false);
    }	

   
}
