package com.ln.tms.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.pojo.Storage;
import com.ln.tms.service.CourierService;
import com.ln.tms.service.StorageService;
import com.ln.tms.shiro.Principal;
import com.ln.tms.util.JsonUtils;


/**
 * StorageController 仓库管理controller
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/storage")
public class StorageController extends BaseController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private CourierService courierService;

    /**
     * 分页多条件查询仓库列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param storage     查询条件
     * @return
     */
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             Storage storage) {
        ModelAndView mv = new ModelAndView("/setting/storage/list");
        storage.setPageCurrent(pageCurrent);
        storage.setPageSize(pageSize);
        mv.addObject("storageList", storageService.queryStoragePage(storage));
        mv.addObject("total", storageService.getStorageTotal(storage));
        mv.addObject("storage", storage);
        return mv;
    }

    /**
     * 页面跳转（查询——新增）
     */
    @RequestMapping(value = "storage_add")
    public ModelAndView addStorage() {
        ModelAndView mv = new ModelAndView("/setting/storage/add");
        mv.addObject("couriers", courierService.queryAll());
        return mv;
    }

    /**
     * 仓库新增保存
     *
     * @param storage 数据
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveStorage(Storage storage) {
        ExecuteResult result = new ExecuteResult();
        storageService.saveStorage(storage);
        return result.jsonReturn(HttpStatus.SC_OK, "保存成功");
    }

    /**
     * 仓库编辑
     *
     * @param storage 数据
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editStorage(Storage storage) {
        ExecuteResult result = new ExecuteResult();
        storageService.editStorage(storage);
        return result.jsonReturn(HttpStatus.SC_OK, "保存成功");
    }

    /**
     * 校验仓库编号
     *
     * @param storageCode 仓库编号
     * @return
     */
    @RequestMapping(value = "checkStorageCode")
    @ResponseBody
    public Map<String, Object> checkStorageCode(String storageCode) {
        ExecuteResult result = new ExecuteResult();
        if (storageService.checkStorageCode(storageCode)) {
            return result.remoteReturn("error", "仓库编号已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }

    /**
     * 校验仓库名称
     *
     * @param storageCode 仓库编号
     * @param storageName 仓库名称
     * @return
     */
    @RequestMapping(value = "{storageCode}/checkStorageName")
    @ResponseBody
    public Map<String, Object> checkStorageName(@PathVariable(value = "storageCode") String storageCode, String storageName) {
        ExecuteResult result = new ExecuteResult();
        if (storageService.checkStorageName(storageCode, storageName)) {
            return result.remoteReturn("error", "仓库名称已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }

    /**
     * 删除仓库
     *
     * @param storageCode 仓库编码
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "storageCode") String storageCode) {
        ExecuteResult result = new ExecuteResult();
        if (StringUtils.isNotBlank(storageCode)) {
            if (storageService.deleteStorageByCode(storageCode)) {
                return result.jsonReturn(HttpStatus.SC_OK);
            } else {
                return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "该仓库已关联用户，不能删除!");
            }
        } else {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }

    /**
     * 查看仓库详情
     *
     * @param storageCode
     * @return
     */
    @RequestMapping(value = "storage_detail", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam(value = "storageCode") String storageCode) {
        if (StringUtils.isBlank(storageCode)) {
            return new ModelAndView(ERROR_VIEW);
        }
        ModelAndView mv = new ModelAndView("/setting/storage/detail");
        mv.addObject("storage", storageService.getStorageBySCode(storageCode));
        return mv;
    }


    /**
     * 修改仓库页面
     *
     * @param storageCode
     * @return
     */
    @RequestMapping(value = "storage_edit", method = RequestMethod.GET)
    public ModelAndView editPage(@RequestParam(value = "storageCode") String storageCode) {
        if (StringUtils.isBlank(storageCode)) {
            return new ModelAndView(ERROR_VIEW);
        }
        ModelAndView mv = new ModelAndView("/setting/storage/edit");
        mv.addObject("storage", storageService.getStorageBySCode(storageCode));
        mv.addObject("couriers", storageService.getStorageCouriers(storageCode));
        return mv;
    }

    /**
     * 根据仓库获取对应的快递公司
     *
     * @param storageCode
     * @return
     */
    @RequestMapping(value = "getCouriers", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getCouriers(@RequestParam(value = "storageCode") String storageCode) {
        return storageService.getCouriers(storageCode);
    }
    
    /**
     * 获取所有仓库的页面
     *
     * @return
     */
    @RequestMapping(value = "getAllStorages", method = RequestMethod.POST)
    public ModelAndView getAllStorages(@RequestParam(value = "storageCode") String storageCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getAllStorages");
    	mv.addObject("storageList", storageService.queryAllNew());
    	mv.addObject("storageCodes", storageCode);
        return mv;
    }
    
    /**
     * 根据选择仓库获取对应的快递公司
     * @param storageCode       仓库编号
     * @param shipperCodeInfo   快递公司Info
     * @param shipperCode       快递公司编号
     * @return
     */
    @RequestMapping(value = "getAllStorageCourier", method = RequestMethod.POST)
    public ModelAndView getAllStorageCourier(@RequestParam(value = "storageCode",required=false) String storageCode,
    		@RequestParam(value = "shipperCodeInfo" ,required=false) String shipperCodeInfo,@RequestParam(value = "shipperCode" ) String shipperCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getAllStorageCouriers");
    	mv.addObject("storages", storageService.queryAllNew());
    	mv.addObject("couriers", courierService.queryAllNew());
    	InfoWhere infoWhere=new  InfoWhere();
    	infoWhere.setStorageCode(storageCode);
    	infoWhere.setShipperCode(shipperCodeInfo);
    	mv.addObject("infoWhere", infoWhere);
    	mv.addObject("storageCourierList", storageService.getAllStorageCourier(infoWhere));	
    	mv.addObject("shipperCodes", shipperCode);
        return mv;
    }
    
    /**
     * 根据选择仓库获取对应的快递公司(综合列表)
     * @param storageCode       仓库编号
     * @param shipperCodeInfo   快递公司Info
     * @param shipperCode       快递公司编号
     * @return
     */
    @RequestMapping(value = "getStorageCouriersByUserIdAll", method = RequestMethod.POST)
    public ModelAndView getStorageCourierByUserId(@RequestParam(value = "storageCode",required=false) String storageCode,
    		@RequestParam(value = "shipperCodeInfo" ,required=false) String shipperCodeInfo,
    		@RequestParam(value = "shipperCode" ) String shipperCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getStorageCouriersByUserIdAll");
    	mv.addObject("storages", storageService.queryAllNew());
    	mv.addObject("couriers", courierService.queryAllNew());
    	InfoWhere infoWhereAll=new  InfoWhere();
    	infoWhereAll.setStorageCode(storageCode);
    	infoWhereAll.setShipperCode(shipperCodeInfo);
    	Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
    	infoWhereAll.setUserId(principal.getUser().getUserId());
    	mv.addObject("infoWhereAll", infoWhereAll);
    	mv.addObject("storageCourierListAll", storageService.getStorageCourierByUserId(infoWhereAll));	
    	mv.addObject("shipperCodesAll", shipperCode);
        return mv;
    }
    
    /**
     * 根据选择仓库获取对应的快递公司(待揽列表)
     * @param storageCode       仓库编号
     * @param shipperCodeInfo   快递公司Info
     * @param shipperCode       快递公司编号
     * @return
     */
    @RequestMapping(value = "getStorageCouriersByUserIdAwaitPut", method = RequestMethod.POST)
    public ModelAndView getStorageCouriersByUserIdAwaitPut(@RequestParam(value = "storageCode",required=false) String storageCode,
    		@RequestParam(value = "shipperCodeInfo" ,required=false) String shipperCodeInfo,
    		@RequestParam(value = "shipperCode" ) String shipperCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getStorageCouriersByUserIdAwaitPut");
    	mv.addObject("storages", storageService.queryAllNew());
    	mv.addObject("couriers", courierService.queryAllNew());
    	InfoWhere infoWhereAwaitPut=new  InfoWhere();
    	infoWhereAwaitPut.setStorageCode(storageCode);
    	infoWhereAwaitPut.setShipperCode(shipperCodeInfo);
    	Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
    	infoWhereAwaitPut.setUserId(principal.getUser().getUserId());
    	mv.addObject("infoWhereAwaitPut", infoWhereAwaitPut);
    	mv.addObject("storageCourierListAwaitPut", storageService.getStorageCourierByUserId(infoWhereAwaitPut));	
    	mv.addObject("shipperCodesAwaitPut", shipperCode);
        return mv;
    }
    
    /**
     * 根据选择仓库获取对应的快递公司(待签列表)
     * @param storageCode       仓库编号
     * @param shipperCodeInfo   快递公司Info
     * @param shipperCode       快递公司编号
     * @return
     */
    @RequestMapping(value = "getStorageCouriersByUserIdSignFor", method = RequestMethod.POST)
    public ModelAndView getStorageCouriersByUserIdSignFor(@RequestParam(value = "storageCode",required=false) String storageCode,
    		@RequestParam(value = "shipperCodeInfo" ,required=false) String shipperCodeInfo,
    		@RequestParam(value = "shipperCode" ) String shipperCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getStorageCouriersByUserIdSignFor");
    	mv.addObject("storages", storageService.queryAllNew());
    	mv.addObject("couriers", courierService.queryAllNew());
    	InfoWhere infoWhereSignFor=new  InfoWhere();
    	infoWhereSignFor.setStorageCode(storageCode);
    	infoWhereSignFor.setShipperCode(shipperCodeInfo);
    	Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
    	infoWhereSignFor.setUserId(principal.getUser().getUserId());
    	mv.addObject("infoWhereSignFor", infoWhereSignFor);
    	mv.addObject("storageCourierListSignFor", storageService.getStorageCourierByUserId(infoWhereSignFor));	
    	mv.addObject("shipperCodesSignFor", shipperCode);
        return mv;
    }
    
    
    /**
     * 根据选择仓库获取对应的快递公司(签收异常列表)
     * @param storageCode       仓库编号
     * @param shipperCodeInfo   快递公司Info
     * @param shipperCode       快递公司编号
     * @return
     */
    @RequestMapping(value = "getStorageCouriersByUserIdSign", method = RequestMethod.POST)
    public ModelAndView getStorageCouriersByUserIdSign(@RequestParam(value = "storageCode",required=false) String storageCode,
    		@RequestParam(value = "shipperCodeInfo" ,required=false) String shipperCodeInfo,
    		@RequestParam(value = "shipperCode" ) String shipperCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getStorageCouriersByUserIdSign");
    	mv.addObject("storages", storageService.queryAllNew());
    	mv.addObject("couriers", courierService.queryAllNew());
    	InfoWhere infoWhereSign=new  InfoWhere();
    	infoWhereSign.setStorageCode(storageCode);
    	infoWhereSign.setShipperCode(shipperCodeInfo);
    	Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
    	infoWhereSign.setUserId(principal.getUser().getUserId());
    	mv.addObject("infoWhereSign", infoWhereSign);
    	mv.addObject("storageCourierListSign", storageService.getStorageCourierByUserId(infoWhereSign));	
    	mv.addObject("shipperCodesSign", shipperCode);
        return mv;
    }
    
    
    
    /**
     * 根据选择仓库获取对应的快递公司(揽收异常列表)
     * @param storageCode       仓库编号
     * @param shipperCodeInfo   快递公司Info
     * @param shipperCode       快递公司编号
     * @return
     */
    @RequestMapping(value = "getStorageCouriersByUserIdTook", method = RequestMethod.POST)
    public ModelAndView getStorageCouriersByUserIdTook(@RequestParam(value = "storageCode",required=false) String storageCode,
    		@RequestParam(value = "shipperCodeInfo" ,required=false) String shipperCodeInfo,
    		@RequestParam(value = "shipperCode" ) String shipperCode) {
    	ModelAndView mv = new ModelAndView("/setting/storage/getStorageCouriersByUserIdTook");
    	mv.addObject("storages", storageService.queryAllNew());
    	mv.addObject("couriers", courierService.queryAllNew());
    	InfoWhere infoWhereTook=new  InfoWhere();
    	infoWhereTook.setStorageCode(storageCode);
    	infoWhereTook.setShipperCode(shipperCodeInfo);
    	Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
    	infoWhereTook.setUserId(principal.getUser().getUserId());
    	mv.addObject("infoWhereTook", infoWhereTook);
    	mv.addObject("storageCourierListTook", storageService.getStorageCourierByUserId(infoWhereTook));	
    	mv.addObject("shipperCodesTook", shipperCode);
        return mv;
    }
    
    /**
     * 查询所有的仓库
     *
     * @return String
     */
    @RequestMapping(value = "queryAll", method = RequestMethod.POST)
    @ResponseBody
    public String queryAll() {
        return JsonUtils.toJson(storageService.queryAllNew());
    }
        
}
