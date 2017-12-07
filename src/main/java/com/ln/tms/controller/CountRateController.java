package com.ln.tms.controller;

import com.ln.tms.bean.CountData;
import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.*;
import com.ln.tms.service.*;
import com.ln.tms.shiro.Principal;
import com.ln.tms.util.JsonUtils;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * LogController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/count")
public class CountRateController extends BaseController {

    @Autowired
    private CountRateService countRateService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private TimeLimitService timeLimitService;

    @Autowired
    private InfoService infoService;

    @Autowired
    private FileupService fileupService;

    /**
     * 查询揽收及时率签收到达率
     *
     * @param type        揽签标识
     * @param storageCode 仓库编号
     * @param startDate   开始时间
     * @param endDate     结束时间
     * @return String
     */
    @RequestMapping(value = "{type}/rate", method = RequestMethod.POST)
    @ResponseBody
    public String getTookSignRate(@PathVariable("type") String type, @RequestParam(value = "storageCode") String storageCode,
                                  String startDate, String endDate) {
        CountRate countRate = new CountRate();
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if (principal == null) {
            return JsonUtils.toJson("loginOut");
        }
        User user = principal.getUser();
        if (StringUtils.isBlank(storageCode)) {
            UserStorage userStorageCourier = infoService.getUserStorageCourier(user.getUserId());
            countRate.setStorageCode(userStorageCourier.getStorageCode());
            countRate.setShipperCode(userStorageCourier.getShipperCode());
        } else {
            countRate.setStorageCode(storageCode);
            countRate.setShipperCode(storageService.queryCourierByUser(new UserStorage(user.getUserId(), storageCode)));
        }
        countRate.setUserId(user.getUserId());
        countRate.setStartDate(startDate);
        countRate.setEndDate(endDate);
        countRate.setType(type);
        List<CountRate> rates = countRateService.queryList(countRate);
        return JsonUtils.toJson(rates);
    }

    /**
     * 查询用户绑定的仓库
     *
     * @return
     */
    @RequestMapping(value = "queryBindUserStorgae", method = RequestMethod.POST)
    @ResponseBody
    public String getBindUserStorage() {
        Principal currentUser = (Principal) SecurityUtils.getSubject().getPrincipal();
        List<Storage> bindUserStorage = storageService.queryBindUserStorage(currentUser.getId());
        return JsonUtils.toJson(bindUserStorage);
    }

    /**
     * 统计平均天数分页多条件查询文件记录列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param fileup      查询条件
     * @return ModelAndView
     */
    @RequestMapping(value = "avgList")
    public ModelAndView avgFileup(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Fileup fileup) {
        ModelAndView mv = new ModelAndView("/count/avgList");
        Principal currentUser = (Principal) SecurityUtils.getSubject().getPrincipal();
        fileup.setUserId(currentUser.getId());
        fileup.setPageCurrent(pageCurrent);
        fileup.setPageSize(pageSize);
        fileup.setBelongTo("3-0");
        mv.addObject("lists", fileupService.queryFileupList(fileup));
        mv.addObject("fileup", fileup);
        mv.addObject("total", fileupService.queryTotal(fileup));
        return mv;
    }

    /**
     * 统计快递综合分析分页多条件查询文件记录列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param fileup      查询条件
     * @return ModelAndView
     */
    @RequestMapping(value = "allList")
    public ModelAndView allFileup(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Fileup fileup) {
        ModelAndView mv = new ModelAndView("/count/allList");
        Principal currentUser = (Principal) SecurityUtils.getSubject().getPrincipal();
        fileup.setUserId(currentUser.getId());
        fileup.setPageCurrent(pageCurrent);
        fileup.setPageSize(pageSize);
        fileup.setBelongTo("4-0");
        mv.addObject("lists", fileupService.queryFileupList(fileup));
        mv.addObject("fileup", fileup);
        mv.addObject("total", fileupService.queryTotal(fileup));
        return mv;
    }

    /**
     * 转向平均天数导出选择页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "avg_select")
    public ModelAndView toSelected() {
        ModelAndView mv = new ModelAndView("/count/avg_select");
        mv.addObject("storages", storageService.queryAllNew());
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
        if (StringUtils.isBlank(storageCode)) {
            return null;
        }
        return storageService.getCouriers(storageCode);
    }

    /**
     * 开始计算平均天数
     *
     * @param countData
     * @return ModelAndView
     */
    @RequestMapping(value = "startAvgDay", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> startAvgDay(CountData countData, HttpServletRequest requet) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        user.setIp(TmsUtils.getRemoteHost(requet));
        countRateService.startAvgDay(countData, user);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK, "后台已开始统计，请2分钟后刷新下载");
    }


    /**
     * 转向快递综合分析导出选择页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "all_select")
    public ModelAndView toAll() {
        ModelAndView mv = new ModelAndView("/count/all_select");
        mv.addObject("storages", storageService.queryAllNew());
        return mv;
    }

    /**
     * 开始计算揽收率，妥投率，三天到达率，及时率
     *
     * @param countData
     * @return ModelAndView
     */
    @RequestMapping(value = "startAllCal", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> startAllCal(CountData countData, HttpServletRequest requet) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        user.setIp(TmsUtils.getRemoteHost(requet));
        countRateService.startAllCal(countData, user);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK, "后台已开始统计，请2分钟后刷新下载");
    }

    /**
     * 删除文件记录和文件
     *
     * @param fileId 文件记录编号
     * @return Map
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "fileId") Long fileId) {
        ExecuteResult result = new ExecuteResult();
        if (fileId == null) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
        try {
            fileupService.deleteFileup(fileId);
            return result.jsonReturn(HttpStatus.SC_OK);
        } catch (Exception e) {
            logger.error("文件日志删除失败", e);
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }

    }
}
