package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.enums.JobType;
import com.ln.tms.pojo.User;
import com.ln.tms.service.TriggerService;
import com.ln.tms.shiro.Principal;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * TriggerController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/trigger")
public class TriggerController extends BaseController {

    @Autowired
    private TriggerService triggerService;

    @RequestMapping(value = "toPage", method = RequestMethod.GET)
    public ModelAndView settingPage() {
        ModelAndView mv = new ModelAndView("/setting/trigger/triggerPage");
        mv.addObject("jobTypes", JobType.values());
        return mv;
    }


    /**
     * 开始计算首页揽收及时率
     *
     * @return Map
     */
    @RequestMapping(value = "tookRateJob", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> startCal(@RequestParam(value = "date") String date,
                                        @RequestParam(value = "jobType") JobType jobType,
                                        HttpServletRequest request) {
        ExecuteResult result = new ExecuteResult();
        if (jobType.getCode() == 0) {
            return result.jsonReturn(HttpStatus.SC_OK, "未选择执行的任务", false);
        }

        if (jobType.getCode() == 1 && StringUtils.isBlank(date)) {
            return result.jsonReturn(HttpStatus.SC_OK, "请选择要统计的日期", false);
        }

        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User user = principal.getUser();
        user.setIp(TmsUtils.getRemoteHost(request));
        triggerService.chooseJob(jobType, date, user);
        return result.jsonReturn(HttpStatus.SC_OK, "手动触发已完成", false);
    }
}
