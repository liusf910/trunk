package com.ln.tms.controller;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.pojo.LogisticsAppointment;
import com.ln.tms.pojo.User;
import com.ln.tms.service.CarrierService;
import com.ln.tms.service.ExportExcelSetService;
import com.ln.tms.service.LogisticsFieldService;
import com.ln.tms.service.LogisticsService;
import com.ln.tms.shiro.Principal;

import net.sf.json.JSONObject;

/**
 * 物流预约
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/logistics")
public class LogisticsController extends BaseController{

	@Autowired
	private LogisticsService logisticsService;
	
	/**
	* 导出字段设置Service
	*/
	@Autowired
	private ExportExcelSetService exportExcelSetService;
	
	@Autowired
	private LogisticsFieldService logisticsFieldService;
	
	@Autowired
	private CarrierService carrierService;
		
	 /**
     * 揽件签收分页多条件查询文件记录列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param logisticsAppointment      查询条件
     * @return ModelAndView
     */
	@RequestMapping(value = "wl/list")
    public ModelAndView LogisticsList(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request,
                                                     LogisticsAppointment logisticsAppointment) {
		  ModelAndView mv = new ModelAndView("/logistics/list");
		  logisticsAppointment.setPageCurrent(pageCurrent);
		  logisticsAppointment.setPageSize(pageSize);
		  mv.addObject("lists", logisticsService.queryLogisticsList(logisticsAppointment));
	      mv.addObject("logisticsAppointment", logisticsAppointment);
	      mv.addObject("total", logisticsService.queryTotal(logisticsAppointment));
	      mv.addObject("carriers", carrierService.queryAll());
	      request.getSession().setAttribute("logisticsAppointment", logisticsAppointment);
		  return mv;
	}
	
	 /**
     * 查看物流预约
     *
     * @param shipperCode 快递编号
     * @param type        跳转页面
     * @return ModelAndView
     */
    @RequestMapping(value = "{oddNum}/show_{type}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable(value = "oddNum") String oddNum, @PathVariable(value = "type") String type) {
        ModelAndView mv = new ModelAndView("/logistics/" + type);
        mv.addObject("logisticsAppointment", logisticsService.queryByOddNum(oddNum));
        return mv;
    }
    
    /**
     * 转向物流预约上传页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "logistics_upload", method = RequestMethod.GET)
    public ModelAndView logisticsPage() {
        return new ModelAndView("/logistics/logistics_upload");
    }

    /**
	 * 转向导出字段选择页面
	 * 
	 * @param exportFields  导出字段Vo
	 * @param type  		took：揽收异常；sign:签收异常 ;all 全部情况
	 * @return ModelAndView  导出字段选择页面
	 */
	@RequestMapping(value = "exportExcelSet", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView exportExcelSet(ExportFields exportFields,String type) {
		ModelAndView mv = new ModelAndView("/logistics/exportExcelSetNew");
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        exportFields.setUserId(user.getUserId());
		ExportFields retExportFields = logisticsFieldService.getExportFieldsByBelongTo(exportFields); // 已选择项
		ExportFields seeExportFields = exportExcelSetService.getExportFieldsByBelongTo("allWl");//显示选择项
		mv.addObject("seeExportFieldList", logisticsFieldService.getSeeExportFieldList(retExportFields,seeExportFields));// 处理显示可导出的字段and是否选择过
		mv.addObject("belongTo", exportFields.getBelongTo());
		mv.addObject("type", type);
		return mv;
	}
	

	/**
	 * 导出Excel
	 * 
	 * @param exportFields 导出字段Vo
	 * @param type  
	 * @param request
	 * @return String  导出文件的文件名
	 */
	@RequestMapping(value = "exportExcel", method = RequestMethod.POST)
	@ResponseBody
    public String exportExcel(ExportFields exportFields,String type, HttpServletRequest request,HttpServletResponse response) {
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        exportFields.setUserId(user.getUserId());
		//检查所属模块查询是否存在，若存在则修改，若不存在则新增.
    	boolean flag=logisticsFieldService.checkExortFields(exportFields);
    	if(flag){//修改
    		logisticsFieldService.updExportFields(exportFields);
    	}else{//新增
    		logisticsFieldService.saveExportFields(exportFields);
    	}
    	//导出
        LogisticsAppointment logisticsAppointment =(LogisticsAppointment) request.getSession().getAttribute("logisticsAppointment"); //从session取查询条件	
    	
        return logisticsFieldService.exportExcle(type,logisticsAppointment,exportFields,response);
    }	
	
	/**
	 * 转向批量修改日期页面
	 * @return
	 */
	@RequestMapping(value="updateView",method = RequestMethod.GET)
	public ModelAndView updateView(){
		return new ModelAndView("/logistics/update");
	}
	
	 /**
     * 批量更新受理送达日期
     *
     * @param ids 编号集合
     * @return Map
     */
    @RequestMapping(value = "updateBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateBatch(@RequestBody JSONObject json) {
    	
    	List<LogisticsAppointment> list = new ArrayList<>();
    	List<String> ids =  (List<String>) json.get("ids");
    	DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
    	Date updateDate = null;
		try {
			updateDate = dateFormat.parse(json.getString("updateDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		};
		Calendar ca=Calendar.getInstance();
		ca.setTime(updateDate);
		ca.add(Calendar.HOUR_OF_DAY, 10);
    	for(int i=0;i<ids.size();i++){
    		LogisticsAppointment logisticsAppointment = new LogisticsAppointment();
    		logisticsAppointment.setAppointmentId(Long.parseLong(ids.get(i)));
    		logisticsAppointment.setLnecAcceptanceDate(ca.getTime());
    		list.add(logisticsAppointment);
    	}
    	logisticsService.updateLogisticsDateList(list);
    	return new ExecuteResult().jsonReturn(HttpStatus.SC_OK,"添加成功");
    }
   
}
