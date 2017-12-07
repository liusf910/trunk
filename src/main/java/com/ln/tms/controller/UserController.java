package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.User;
import com.ln.tms.service.*;
import com.ln.tms.shiro.Principal;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * UserController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourierService courierService;

    @Autowired
    private LogService logService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StorageService storageService;

    /**
     * 分页多条件查询用户列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param user        查询条件
     * @return ModelAndView
     */
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, User user) {
        ModelAndView mv = new ModelAndView("/setting/user/list");
        user.setPageCurrent(pageCurrent);
        user.setPageSize(pageSize);
        mv.addObject("lists", userService.queryUserList(user));
        mv.addObject("user", user);
        mv.addObject("total", userService.queryTotal(user));
        mv.addObject("couriers", courierService.queryAll());
        mv.addObject("storages", storageService.queryAllNew());
        return mv;
    }

    /**
     * 转向用户新增页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "user_add", method = RequestMethod.GET)
    public ModelAndView addPage() {
        ModelAndView mv = new ModelAndView("/setting/user/add");
        mv.addObject("couriers", courierService.queryAll());
        mv.addObject("roles", roleService.queryAll());
        mv.addObject("storages", storageService.queryAllNew());
        return mv;
    }

    /**
     * 添加或修改用户信息
     *
     * @param user 数据
     * @return Map
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(@RequestParam(value = "logonPwd2", required = false) String logonPwd2, User user, HttpServletRequest request) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        ExecuteResult result = new ExecuteResult();
        if (principal == null) {
            return result.jsonReturn(HttpStatus.SC_MOVED_PERMANENTLY, "登录超时!");
        }
        if (user.getLogonPwd().length() < 6) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "密码长度小于6位,请重新输入!");
        }
        if (!(logonPwd2.equals(user.getLogonPwd()))) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "重复密码不一致,请重新输入!");
        }
        user.setCreateId(principal.getId());
        userService.saveUser(user);
        return result.jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 修改用户信息
     *
     * @param user 数据
     * @return Map
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(@RequestParam(value = "logonPwd2", required = false) String logonPwd2, User user, HttpServletRequest request) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        ExecuteResult result = new ExecuteResult();
        if (principal == null) {
            return result.jsonReturn(HttpStatus.SC_MOVED_PERMANENTLY, "登录超时!");
        }
        if (StringUtils.isNotBlank(user.getUserId())) {
            user.setLastId(principal.getId());
            userService.editUser(user);
        }
        return result.jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 转向用户修改密码页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "changePwd", method = RequestMethod.GET)
    public ModelAndView changePwd() {
        return new ModelAndView("/setting/user/change_pwd");
    }

    /**
     * 更改用户密码
     *
     * @param oldPwd  旧密码
     * @param newPwd  新密码
     * @param newPwd2 重复密码
     * @param request 请求
     * @return Map
     */
    @RequestMapping(value = "editUserPwd", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateUserPwd(@RequestParam(value = "oldPwd") String oldPwd,
                                             @RequestParam(value = "newPwd") String newPwd,
                                             @RequestParam(value = "newPwd2") String newPwd2,
                                             HttpServletRequest request) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User loginUser = userService.queryById(principal.getId());
        ExecuteResult result = new ExecuteResult();
        if (loginUser == null) {
            return result.jsonReturn(HttpStatus.SC_MOVED_PERMANENTLY, "登录超时!");
        }
        if (!(loginUser.getLogonPwd().equals(oldPwd))) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "原密码输入错误!");
        }
        if (newPwd.length() < 6) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "密码长度小于6位,请重新输入!");
        }
        if (!(newPwd2.equals(newPwd))) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "重复密码不一致,请重新输入!");
        }
        loginUser.setLogonPwd(newPwd);
        userService.updateSelective(loginUser);
        return result.jsonReturn(HttpStatus.SC_OK);
    }


    /**
     * 删除用户
     *
     * @param id 用户编号
     * @return Map
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "id") String id) {
        ExecuteResult result = new ExecuteResult();
        if (StringUtils.isNotBlank(id)) {
            userService.deleteByIdLJ(id);
            return result.jsonReturn(HttpStatus.SC_OK);
        } else {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }

    /**
     * 修改用户页面
     *
     * @param id 用户编号
     * @return ModelAndView
     */
    @RequestMapping(value = "user_edit", method = RequestMethod.GET)
    public ModelAndView editPage(@RequestParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return new ModelAndView(ERROR_VIEW);
        }
        ModelAndView mv = new ModelAndView("/setting/user/edit");
        mv.addObject("user", userService.queryByIdNew(id));
        mv.addObject("roles", userService.userBingRole(id));
        return mv;
    }

    /**
     * 查看用户详情
     *
     * @param id 用户编号
     * @return ModelAndView
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return new ModelAndView(ERROR_VIEW);
        }
        ModelAndView mv = new ModelAndView("/setting/user/detail");
        mv.addObject("user", userService.queryByIdNew(id));
        return mv;
    }


    /**
     * 校验用户登录名称
     *
     * @param logonName 登录名
     * @return Map
     */
    @RequestMapping(value = "{userId}/checkUserName")
    @ResponseBody
    public Map<String, Object> checkUserName(@PathVariable(value = "userId") String userId, String logonName) {
        ExecuteResult result = new ExecuteResult();
        if (userService.checkLogonName(userId, logonName)) {
            return result.remoteReturn("error", "登录名已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }
}
