package com.ln.tms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.StateType;
import com.ln.tms.enums.TookType;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.User;
import com.ln.tms.service.CourierService;
import com.ln.tms.service.ExportExcelSetService;
import com.ln.tms.service.InfoService;
import com.ln.tms.service.MonitoService;
import com.ln.tms.service.StorageService;
import com.ln.tms.service.UserService;
import com.ln.tms.shiro.Principal;

/**
 * MonitoController 后台管理监测controller
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/monito")
public class MonitoController extends BaseController {

	/**
	 * 监测管理Service
	 */
	@Autowired
	private MonitoService monitoService;
    
	/**
	 * 用户Service
	 */
	@Autowired
	private UserService userService;
   
	/**
	 * 快递公司Service
	 */
	@Autowired
	private CourierService courierService;
	
	/**
	 * 仓库Service
	 */
	@Autowired
	private StorageService storageService;
	
	
	 @Autowired
	 private InfoService infoService;
	 
	 /**
	 * 导出字段设置Service
	 */
	@Autowired
	private ExportExcelSetService exportExcelSetService;

	
	//20170417 start
		/**
	     * 全部速递列表
	     *
	     * @param page        当前页码
	     * @param rows        页面条数
	     * @param infoWhere   查询条件
	     * @return ModelAndView   全部速递列表页面
	     */
	    @RequestMapping(value = "all/list")
	    public ModelAndView allList(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
	                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
	                                     HttpServletRequest request,InfoWhere infoWhere){
	        ModelAndView mv = new ModelAndView("monito/allList");
	        PageInfo<Info> list = infoService.getInfoPageList(infoWhere, page, rows);
	        mv.addObject("pageInfo", list);
	        mv.addObject("infoWhere", infoWhere);
	        mv.addObject("storages", storageService.queryAllNew());
	        mv.addObject("couriers", courierService.queryAllNew());
	        mv.addObject("states", StateType.values());
	        mv.addObject("orderStatus", OrderType.values());
	        mv.addObject("tookReasons", TookType.values());
	        mv.addObject("signReasons", SignType.values());
	        request.getSession().setAttribute("infoWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
	        return mv;
	    }
	    
	    /**
	     * 全部揽收异常列表
	     *
	     * @param page        当前页码
	     * @param rows        页面条数
	     * @param infoWhere   查询条件
	     * @return ModelAndView 全部揽收异常列表页面
	     */
	    @RequestMapping(value = "allTook/list")
	    public ModelAndView allTookList(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
	                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
	                                     HttpServletRequest request,InfoWhere infoWhere) {
	        ModelAndView mv = new ModelAndView("monito/allTookList");
	        infoWhere.setLsOver(true);
	        PageInfo<Info> list = infoService.getInfoPageList(infoWhere, page, rows);
	        mv.addObject("pageInfo", list);
	        mv.addObject("infoWhere", infoWhere);
	        mv.addObject("tookReasons", TookType.values());
	        mv.addObject("states", StateType.values());
	        mv.addObject("storages", storageService.queryAllNew());
	        mv.addObject("couriers", courierService.queryAllNew());
	        request.getSession().setAttribute("infoTookWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
	        return mv;
	    }
	    
	    /**
	     * 全部签收异常列表
	     *
	     * @param page         当前页码
	     * @param rows         页面条数
	     * @param infoWhere    查询条件
	     * @return ModelAndView   全部签收异常列表页面
	     */
	    @RequestMapping(value = "allSign/list")
	    public ModelAndView allSignList(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
	                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
	                                     HttpServletRequest request,InfoWhere infoWhere) {
	        ModelAndView mv = new ModelAndView("monito/allSignList");
	        infoWhere.setQsOver(true);
	        PageInfo<Info> list = infoService.getInfoPageList(infoWhere, page, rows);
	        mv.addObject("pageInfo", list);
	        mv.addObject("infoWhere", infoWhere);
	        mv.addObject("storages", storageService.queryAllNew());
	        mv.addObject("couriers", courierService.queryAllNew());
	        mv.addObject("states", StateType.values());
	        mv.addObject("orderStatus", OrderType.values());
	        mv.addObject("signReasons", SignType.values());
	        request.getSession().setAttribute("infoSignWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
	        return mv;
	    }
	//20170417end
	    
	//20170424start
	    /**
	     * 全部待揽提示
	     *
	     * @param page         当前页码
	     * @param rows         页面条数
	     * @param infoWhere    查询条件
	     * @return ModelAndView   全部待揽提示列表页面
	     */
	    @RequestMapping(value = "allAwaitPut/list")
	    public ModelAndView allAwaitPut(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
	                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
	                                     HttpServletRequest request,InfoWhere infoWhere) {
	    	infoWhere.setState(StateType.DSC);
	        ModelAndView mv = new ModelAndView("monito/allAwaitPutList");
	        PageInfo<Info> list = infoService.getInfoPageList(infoWhere, page, rows);
	        mv.addObject("pageInfo", list);
	        mv.addObject("infoWhere", infoWhere);
	        mv.addObject("storages", storageService.queryAllNew());
	        mv.addObject("couriers", courierService.queryAllNew());
	        mv.addObject("states", StateType.values());
	        mv.addObject("orderStatus", OrderType.values());
	        mv.addObject("signReasons", SignType.values());
	        request.getSession().setAttribute("infoAwaitPutWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
	        return mv;
	    }
	    
	    /**
	     * 全部待签提示
	     *
	     * @param page         当前页码
	     * @param rows         页面条数
	     * @param infoWhere    查询条件
	     * @return ModelAndView   全部待签提示列表页面
	     */
	    @RequestMapping(value = "allSignFor/list")
	    public ModelAndView allSignFor(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
	                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
	                                     HttpServletRequest request,InfoWhere infoWhere) {
	    	infoWhere.setSignFor(true);
	        ModelAndView mv = new ModelAndView("monito/allSignForList");
	        PageInfo<Info> list = infoService.getInfoPageList(infoWhere, page, rows);
	        mv.addObject("pageInfo", list);
	        mv.addObject("infoWhere", infoWhere);
	        mv.addObject("storages", storageService.queryAllNew());
	        mv.addObject("couriers", courierService.queryAllNew());
	        mv.addObject("states", StateType.values());
	        mv.addObject("orderStatus", OrderType.values());
	        mv.addObject("signReasons", SignType.values());
	        request.getSession().setAttribute("infoSignForWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
	        return mv;
	    }
	//20170424end

	/**
	 * 查看快递详情页面
	 * 
	 * @param infoId  快递id
	 * @return ModelAndView 快递详情页面
	 */
	@RequestMapping(value = "{infoId}/monito_detail", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable(value = "infoId") String infoId) {
		ModelAndView mv = new ModelAndView("/monito/monito_detail");
		mv.addObject("monito", monitoService.queryInfoByInfoId(infoId));
		return mv;
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
		ModelAndView mv = new ModelAndView("/monito/exportExcelSetNew");
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        exportFields.setUserId(user.getUserId());
		ExportFields retExportFields = monitoService.getExportFieldsByBelongTo(exportFields); // 已选择项
		ExportFields seeExportFields = exportExcelSetService.getExportFieldsByBelongTo("all");//显示选择项
		mv.addObject("seeExportFieldList", monitoService.getSeeExportFieldList(retExportFields,seeExportFields));// 处理显示可导出的字段and是否选择过
		mv.addObject("belongTo", exportFields.getBelongTo());
		mv.addObject("type", type);
		return mv;
	}

	/**
	 * 导出Excel
	 * 
	 * @param exportFields 导出字段Vo
	 * @param type  took：揽收异常；sign:签收异常;all：全部情况 awaitPut：待揽提示 ；signFor 待签提示
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
    	boolean flag=monitoService.checkExortFields(exportFields);
    	if(flag){//修改
    		monitoService.updExportFields(exportFields);
    	}else{//新增
    		monitoService.saveExportFields(exportFields);
    	}
    	//导出
    	InfoWhere infoWhere= null;
    	if("all".equals(type)){ //全部快递
    		infoWhere=(InfoWhere) request.getSession().getAttribute("infoWhere"); //从session取查询条件	
    	}else if("took".equals(type)){//揽收异常
    		infoWhere=(InfoWhere) request.getSession().getAttribute("infoTookWhere"); //从session取查询条件	
    	}else if("sign".equals(type)){//签收异常
    		infoWhere=(InfoWhere) request.getSession().getAttribute("infoSignWhere"); //从session取查询条件	
    	}else if("awaitPut".equals(type)){//待揽提示
    		infoWhere=(InfoWhere) request.getSession().getAttribute("infoAwaitPutWhere"); //从session取查询条件	
    	}else{//待签提示
    		infoWhere=(InfoWhere) request.getSession().getAttribute("infoSignForWhere"); //从session取查询条件	
    	}
    	return monitoService.exportExcle(type,infoWhere,exportFields,response);
    }	
	
	
}

