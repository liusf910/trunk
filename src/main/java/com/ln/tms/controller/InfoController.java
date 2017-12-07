package com.ln.tms.controller;

import com.github.pagehelper.PageInfo;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.StateType;
import com.ln.tms.enums.TookType;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.User;
import com.ln.tms.pojo.UserStorage;
import com.ln.tms.service.*;
import com.ln.tms.shiro.Principal;
import com.ln.tms.util.ToMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * InfoController - 快递信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings({"SpringJavaAutowiringInspection", "Duplicates"})
@RestController
@RequestMapping("monitoInfo")
public class InfoController extends BaseController {

    @Autowired
    private InfoSolrService infoSolrService;
    /**
     * 快递service
     */
    @Autowired
    private InfoService infoService;

    /**
     * 用户service
     */
    @Autowired
    private UserService userService;

    /**
     * 监测管理Service
     */
    @Autowired
    private MonitoService monitoService;

    /**
     * 导出字段设置Service
     */
    @Autowired
    private ExportExcelSetService exportExcelSetService;

    /**
     * 综合列表
     *
     * @param page      页面
     * @param rows      条数
     * @param infoWhere 查询条件
     * @param request   HttpServletRequest
     * @return ModelAndView  综合列表页面
     */
    @RequestMapping(value = "yuantong/list")
    public ModelAndView yuanTongList(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
                                     HttpServletRequest request, InfoWhere infoWhere) {
        ModelAndView mv = new ModelAndView("monitoInfo/list");
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(infoWhere.getShipperCode())) { //取登录用户的仓库-快递公司，并格式化（'9021-10097'）
            String[] stoageShipperCodeArr = userService.queryByIdNew(principal.getUser().getUserId()).getShipperCode().split(",");
            String shipperCode = "";
            for (String str : stoageShipperCodeArr) {
                shipperCode += "'" + str + "',";
            }
            infoWhere.setShipperCode(shipperCode.substring(0, shipperCode.length() - 1));
            infoWhere.setCourierName(userService.queryByIdNew(principal.getUser().getUserId()).getCourierName());
        }
        PageInfo<Info> list;
        infoWhere.setUserId(principal.getUser().getUserId());//快递公司要取登录用户的userId
        Map<String, Object> map = ToMapUtils.beanToMap(infoWhere);
        if (null != infoWhere.getLogisticCode() && !"".equals(infoWhere.getLogisticCode())) {
            list = infoSolrService.singleSolrFind(map, page, rows);
        } else {
            list = infoSolrService.solrFind(map, page, rows);
        }
        mv.addObject("pageInfo", list);
        mv.addObject("infoWhere", infoWhere);
        mv.addObject("states", StateType.values());
        mv.addObject("orderStatus", OrderType.values());
        mv.addObject("tookReasons", TookType.values());
        mv.addObject("signReasons", SignType.values());
        request.getSession().setAttribute("yuantongInfoWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
        return mv;
    }

    /**
     * 待揽提示
     *
     * @param page      页面
     * @param rows      条数
     * @param infoWhere 查询条件
     * @param request   HttpServletRequest
     * @return ModelAndView  待揽提示页面
     */
    @RequestMapping(value = "yuantong/awaitPut")
    public ModelAndView awaitPut(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
                                 HttpServletRequest request, InfoWhere infoWhere) {
        ModelAndView mv = new ModelAndView("monitoInfo/awaitput-list");
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        infoWhere.setState(StateType.DSC);
        if (StringUtils.isBlank(infoWhere.getShipperCode())) {
            String[] stoageShipperCodeArr = userService.queryByIdNew(principal.getUser().getUserId()).getShipperCode().split(",");
            String shipperCode = "";
            for (String str : stoageShipperCodeArr) {
                shipperCode += "'" + str + "',";
            }
            infoWhere.setShipperCode(shipperCode.substring(0, shipperCode.length() - 1));
            infoWhere.setCourierName(userService.queryByIdNew(principal.getUser().getUserId()).getCourierName());
        }
        infoWhere.setUserId(principal.getUser().getUserId());//快递公司要取登录用户的userId
        PageInfo<Info> list = null;
        if (null != infoWhere.getLogisticCode() && !"".equals(infoWhere.getLogisticCode())) {
            list = infoService.getInfoPageList(infoWhere, page, rows);
        } else {
            Map<String, Object> map = ToMapUtils.beanToMap(infoWhere);
            list = infoSolrService.solrFind(map, page, rows);
        }

        mv.addObject("pageInfo", list);
        mv.addObject("infoWhere", infoWhere);
        mv.addObject("states", StateType.values());
        mv.addObject("orderStatus", OrderType.values());
        mv.addObject("tookReasons", TookType.values());
        mv.addObject("signReasons", SignType.values());
        request.getSession().setAttribute("yuantongAwaitPutInfoWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
        return mv;
    }

    /**
     * 待签提示
     *
     * @param page      页面
     * @param rows      条数
     * @param infoWhere 查询条件
     * @param request   HttpServletRequest
     * @return ModelAndView  待签提示页面
     */
    @RequestMapping(value = "yuantong/signFor")
    public ModelAndView signFor(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
                                HttpServletRequest request, InfoWhere infoWhere) {
        ModelAndView mv = new ModelAndView("monitoInfo/signfor-list");
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        infoWhere.setSignFor(true);
        if (StringUtils.isBlank(infoWhere.getShipperCode())) {
            String[] stoageShipperCodeArr = userService.queryByIdNew(principal.getUser().getUserId()).getShipperCode().split(",");
            String shipperCode = "";
            for (String str : stoageShipperCodeArr) {
                shipperCode += "'" + str + "',";
            }
            infoWhere.setShipperCode(shipperCode.substring(0, shipperCode.length() - 1));
            infoWhere.setCourierName(userService.queryByIdNew(principal.getUser().getUserId()).getCourierName());
        }
        PageInfo<Info> list;
        infoWhere.setUserId(principal.getUser().getUserId());//快递公司要取登录用户的userId
        if (null != infoWhere.getLogisticCode() && !"".equals(infoWhere.getLogisticCode())) {
            list = infoService.getInfoPageList(infoWhere, page, rows);
        } else {
            Map<String, Object> map = ToMapUtils.beanToMap(infoWhere);
            list = infoSolrService.solrFind(map, page, rows);
        }
        mv.addObject("pageInfo", list);
        mv.addObject("infoWhere", infoWhere);
        mv.addObject("states", StateType.values());
        mv.addObject("orderStatus", OrderType.values());
        mv.addObject("tookReasons", TookType.values());
        mv.addObject("signReasons", SignType.values());
        request.getSession().setAttribute("yuantongSignForInfoWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
        return mv;
    }

    /**
     * 揽收异常
     *
     * @param page      页面
     * @param rows      条数
     * @param infoWhere 查询条件
     * @param request   HttpServletRequest
     * @return ModelAndView  揽收异常页面
     */
    @RequestMapping(value = "yuantong/abnormalTaking")
    public ModelAndView abnormalTaking(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
                                       HttpServletRequest request, InfoWhere infoWhere) {
        ModelAndView mv = new ModelAndView("monitoInfo/taking-list");
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        infoWhere.setLsOver(true);
        if (StringUtils.isBlank(infoWhere.getShipperCode())) {
            String[] stoageShipperCodeArr = userService.queryByIdNew(principal.getUser().getUserId()).getShipperCode().split(",");
            String shipperCode = "";
            for (String str : stoageShipperCodeArr) {
                shipperCode += "'" + str + "',";
            }
            infoWhere.setShipperCode(shipperCode.substring(0, shipperCode.length() - 1));
            infoWhere.setCourierName(userService.queryByIdNew(principal.getUser().getUserId()).getCourierName());
        }
        PageInfo<Info> list;
        infoWhere.setUserId(principal.getUser().getUserId());//快递公司要取登录用户的userId
        if (null != infoWhere.getLogisticCode() && !"".equals(infoWhere.getLogisticCode())) {
            list = infoService.getInfoPageList(infoWhere, page, rows);
        } else {
            Map<String, Object> map = ToMapUtils.beanToMap(infoWhere);
            list = infoSolrService.solrFind(map, page, rows);
        }
        mv.addObject("pageInfo", list);
        mv.addObject("infoWhere", infoWhere);
        mv.addObject("states", StateType.values());
        mv.addObject("orderStatus", OrderType.values());
        mv.addObject("tookReasons", TookType.values());
        mv.addObject("signReasons", SignType.values());
        request.getSession().setAttribute("yuantongTookInfoWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
        return mv;
    }

    /**
     * 签收异常
     *
     * @param page      页面
     * @param rows      条数
     * @param infoWhere 查询条件
     * @param request   HttpServletRequest
     * @return ModelAndView  签收异常页面
     */
    @RequestMapping(value = "yuantong/abnormalSign")
    public ModelAndView abnormalSign(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer page,
                                     @RequestParam(value = "pageSize", defaultValue = "30") Integer rows,
                                     HttpServletRequest request, InfoWhere infoWhere) {
        ModelAndView mv = new ModelAndView("monitoInfo/signin-list");
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        infoWhere.setQsOver(true);
        if (StringUtils.isBlank(infoWhere.getShipperCode())) {
            String[] stoageShipperCodeArr = userService.queryByIdNew(principal.getUser().getUserId()).getShipperCode().split(",");
            String shipperCode = "";
            for (String str : stoageShipperCodeArr) {
                shipperCode += "'" + str + "',";
            }
            infoWhere.setShipperCode(shipperCode.substring(0, shipperCode.length() - 1));
            infoWhere.setCourierName(userService.queryByIdNew(principal.getUser().getUserId()).getCourierName());
        }
        PageInfo<Info> list;
        infoWhere.setUserId(principal.getUser().getUserId());//快递公司要取登录用户的userId
        if (null != infoWhere.getLogisticCode() && !"".equals(infoWhere.getLogisticCode())) {
            list = infoService.getInfoPageList(infoWhere, page, rows);
        } else {
            Map<String, Object> map = ToMapUtils.beanToMap(infoWhere);
            list = infoSolrService.solrFind(map, page, rows);
        }
        mv.addObject("pageInfo", list);
        mv.addObject("infoWhere", infoWhere);
        mv.addObject("states", StateType.values());
        mv.addObject("orderStatus", OrderType.values());
        mv.addObject("tookReasons", TookType.values());
        mv.addObject("signReasons", SignType.values());
        request.getSession().setAttribute("yuantongSignInfoWhere", infoWhere);// 把查询条件放在session中，用于导出数据使用.
        return mv;
    }

    /**
     * 转向导出字段选择页面
     *
     * @param exportFields 导出字段Vo
     * @param type         took：揽收异常；sign:签收异常 ;all 全部情况
     * @return ModelAndView  导出字段选择页面
     */
    @RequestMapping(value = "exportExcelSet", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView exportExcelSet(ExportFields exportFields, String type) {
        ModelAndView mv = new ModelAndView("/monitoInfo/exportExcelSetNew");
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        exportFields.setUserId(user.getUserId());
        ExportFields retExportFields = monitoService.getExportFieldsByBelongTo(exportFields); // 已选择项
        ExportFields seeExportFields = exportExcelSetService.getExportFieldsByBelongTo("courier");//显示选择项
        mv.addObject("seeExportFieldList", monitoService.getSeeExportFieldList(retExportFields, seeExportFields));// 处理显示可导出的字段and是否选择过
        mv.addObject("belongTo", exportFields.getBelongTo());
        mv.addObject("type", type);
        return mv;
    }


    /**
     * 导出Excel
     *
     * @param exportFields 导出字段Vo
     * @param type         yuantongInfoWhere：圆通综合列表；yuantongAwaitPutInfoWhere:圆通待揽提示;
     *                     yuantongTookInfoWhere： 圆通揽收异常; yuantongSignForInfoWhere圆通待签提示；
     *                     yuantongSignInfoWhere： 圆通签收异常.
     * @param request
     * @return String  导出文件的文件名
     */
    @RequestMapping(value = "exportExcel", method = RequestMethod.POST)
    @ResponseBody
    public String exportExcel(ExportFields exportFields, String type, HttpServletRequest request, HttpServletResponse response) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        exportFields.setUserId(user.getUserId());
        //检查所属模块查询是否存在，若存在则修改，若不存在则新增.
        boolean flag = monitoService.checkExortFields(exportFields);
        if (flag) {//修改
            monitoService.updExportFields(exportFields);
        } else {//新增
            monitoService.saveExportFields(exportFields);
        }
        //导出
        InfoWhere infoWhere = null;
        if ("yuantongInfoWhere".equals(type)) { //圆通综合列表
            infoWhere = (InfoWhere) request.getSession().getAttribute("yuantongInfoWhere"); //从session取查询条件
        } else if ("yuantongAwaitPutInfoWhere".equals(type)) {//圆通待揽提示
            infoWhere = (InfoWhere) request.getSession().getAttribute("yuantongAwaitPutInfoWhere"); //从session取查询条件
        } else if ("yuantongTookInfoWhere".equals(type)) {//圆通揽收异常
            infoWhere = (InfoWhere) request.getSession().getAttribute("yuantongTookInfoWhere"); //从session取查询条件
        } else if ("yuantongSignForInfoWhere".equals(type)) {//圆通待签提示
            infoWhere = (InfoWhere) request.getSession().getAttribute("yuantongSignForInfoWhere"); //从session取查询条件
        } else {//圆通签收异常
            infoWhere = (InfoWhere) request.getSession().getAttribute("yuantongSignInfoWhere"); //从session取查询条件
        }
        //赋值登录用户的快递公司和仓库
        UserStorage userStorageCourier = infoService.getUserStorageCourier(user.getUserId());
        if (StringUtils.isBlank(infoWhere.getShipperCode())) {
            infoWhere.setShipperCode(userStorageCourier.getShipperCode());
        }
        if (StringUtils.isBlank(infoWhere.getStorageCode())) {
            infoWhere.setStorageCode(userStorageCourier.getStorageCode());
        }
        return monitoService.exportExcle(type, infoWhere, exportFields, response);
    }

}