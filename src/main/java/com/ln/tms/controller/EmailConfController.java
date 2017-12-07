package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.EmailConf;
import com.ln.tms.service.EmailConfService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * EmailConfController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/email")
public class EmailConfController extends BaseController {

    @Autowired
    private EmailConfService emailConfService;

    /**
     * 转向邮件设置页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "toPage", method = RequestMethod.GET)
    public ModelAndView settingPage() {
        ModelAndView mv = new ModelAndView("/setting/email/config");
        List<EmailConf> emailConfs = emailConfService.queryAll();
        mv.addObject("emailConf", emailConfs.size() == 0 ? null : emailConfs.get(0));
        return mv;
    }

    /**
     * 修改邮件配置
     *
     * @param emailConf 邮件配置
     * @return Map
     */
    @RequestMapping(value = "editConfig", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(EmailConf emailConf) {
        emailConfService.saveOrUpdate(emailConf);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK, "操作成功", false);
    }
}
