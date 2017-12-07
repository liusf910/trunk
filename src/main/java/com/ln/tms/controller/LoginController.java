package com.ln.tms.controller;

import com.ln.tms.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * UserController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param logonName          登录名
     * @param logonPwd           登录密码
     * @param vcode              验证码
     * @param redirectAttributes 瞬时消息
     * @param request            请求
     * @return String
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(String logonName, String logonPwd, String vcode, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (StringUtils.isBlank(logonName) || StringUtils.isBlank(logonPwd)) {
            redirectAttributes.addFlashAttribute("message", "用户名密码不能为空");
            return "redirect:/page/login";
        }
        if (StringUtils.isBlank(vcode)) {
            redirectAttributes.addFlashAttribute("message", "验证码不能为空");
            return "redirect:/page/login";
        }
        UsernamePasswordToken token = new UsernamePasswordToken(logonName, logonPwd);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            redirectAttributes.addFlashAttribute("message", "未知账户");
        } catch (IncorrectCredentialsException ice) {
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        } catch (LockedAccountException lae) {
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        } catch (DisabledAccountException eae) {
            redirectAttributes.addFlashAttribute("message", "账户已过期");
        } catch (AuthenticationException ae) {
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }
        //验证码验证
        Session session = SecurityUtils.getSubject().getSession();
        vcode = vcode.toLowerCase();
        String imgvcode = (String) session.getAttribute("validateCode");
        session.removeAttribute("validateCode");
        if (!vcode.equals(imgvcode)) {
            redirectAttributes.addFlashAttribute("message", "验证码不正确");
            return "redirect:/page/login";
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            return "redirect:/page/index";
        } else {
            token.clear();
            return "redirect:/page/login";
        }
    }

    /**
     * 注销登录
     *
     * @param redirectAttributes
     * @return String
     */
    @RequestMapping(value = "loginOut", method = RequestMethod.GET)
    public String loginOut(RedirectAttributes redirectAttributes) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/page/login";
    }
}
