package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.Courier;
import com.ln.tms.service.CourierService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * LoginController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/courier")
public class CourierController extends BaseController {

    @Autowired
    private CourierService courierService;


    /**
     * 分页多条件查询快递列表
     *
     * @param pageCurrent 页面
     * @param pageSize    条数
     * @param courier     查询条件
     * @return ModelAndView
     */
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Courier courier) {
        ModelAndView mv = new ModelAndView("/setting/courier/list");
        mv.addObject("pageInfo", courierService.queryCourierPage(courier, pageCurrent, pageSize));
        mv.addObject("courier", courier);
        return mv;
    }

    /**
     * 转向快递新增页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "courier_add", method = RequestMethod.GET)
    public ModelAndView addPage() {
        return new ModelAndView("/setting/courier/add");
    }

    /**
     * 添加快递信息
     *
     * @param courier
     * @return Map
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Courier courier) {
        if (courierService.queryById(courier.getShipperCode()) != null) {
            return new ExecuteResult().jsonReturn(300, "快递编号已存在!!!");
        }
        courierService.saveSelective(courier);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 改快递信息
     *
     * @param courier
     * @return Map
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Courier courier) {
        courierService.updateSelective(courier);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 删除快递
     *
     * @param shipperCode 快递编号
     * @return Map
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "shipperCode") String shipperCode) {
        ExecuteResult result = new ExecuteResult();
        if (StringUtils.isNotBlank(shipperCode)) {
            if (courierService.isBindStorageCourier(shipperCode)) {
                return result.jsonReturn(HttpStatus.SC_OK);
            } else {
                return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "该快递公司已关联仓库，不能删除!");
            }
        } else {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }

    /**
     * 查看或修改快递页面
     *
     * @param shipperCode 快递编号
     * @param type        跳转页面
     * @return ModelAndView
     */
    @RequestMapping(value = "{shipperCode}/show_{type}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable(value = "shipperCode") String shipperCode, @PathVariable(value = "type") String type) {
        ModelAndView mv = new ModelAndView("/setting/courier/" + type);
        mv.addObject("courier", courierService.queryById(shipperCode));
        return mv;
    }


    /**
     * 校验快递公司编号
     *
     * @param shipperCode 快递编号
     * @return Map
     */
    @RequestMapping(value = "checkShipperCode")
    @ResponseBody
    public Map<String, Object> checkShipperCode(String shipperCode) {
        ExecuteResult result = new ExecuteResult();
        if (courierService.checkShipperCode(shipperCode)) {
            return result.remoteReturn("error", "快递公司编号已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }

    /**
     * 校验快递公司名称
     *
     * @param shipperCode 快递编号
     * @param courierName 快递公司名称
     * @return Map
     */
    @RequestMapping(value = "{shipperCode}/checkCourierName")
    @ResponseBody
    public Map<String, Object> checkCourierName(@PathVariable(value = "shipperCode") String shipperCode, String courierName) {
        ExecuteResult result = new ExecuteResult();
        if (courierService.checkCourierName(shipperCode, courierName)) {
            return result.remoteReturn("error", "快递公司名称已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }
}
