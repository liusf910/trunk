package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.Role;
import com.ln.tms.service.PermissionService;
import com.ln.tms.service.RolePopedomService;
import com.ln.tms.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * RoleController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePopedomService rolePopedomService;


    /**
     * 分页多条件查询角色列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param role        条件
     * @return ModelAndView
     */
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Role role) {
        ModelAndView mv = new ModelAndView("/setting/role/list");
        mv.addObject("pageInfo", roleService.queryRolePage(role, pageCurrent, pageSize));
        mv.addObject("role", role);
        return mv;
    }

    /**
     * 转向角色新增页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "role_add", method = RequestMethod.GET)
    public ModelAndView addPage() {
        ModelAndView mv = new ModelAndView("/setting/role/add");
        //获取所有的权限
        mv.addObject("permissionAll", permissionService.queryAll());
        return mv;
    }

    /**
     * 角色新增
     *
     * @param menus 权限编号
     * @return Map
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addRole(@RequestParam(value = "menus") String menus, Role role) {
        roleService.saveRole(role, menus);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 角色编辑
     *
     * @param menus 权限编号
     * @return Map
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editRole(@RequestParam(value = "menus") String menus, Role role) {
        if (StringUtils.isNotBlank(role.getRoleId())) {
            roleService.editRole(role, menus);
        }
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 转向角色编辑页面
     *
     * @param roleId 角色编号
     * @return ModelAndView
     */
    @RequestMapping(value = "role_edit", method = RequestMethod.GET)
    public ModelAndView editPage(@RequestParam(value = "roleId") String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return new ModelAndView(ERROR_VIEW);
        }
        ModelAndView mv = new ModelAndView("/setting/role/edit");
        Map<String, Object> resultMap = roleService.RoleBingPermission(roleId);
        mv.addObject("pmsList", resultMap.get("lists"));
        mv.addObject("menus", resultMap.get("menus"));
        mv.addObject("role", roleService.queryById(roleId));
        return mv;
    }

    /**
     * 删除角色
     *
     * @param roleId 角色编号
     * @return Map
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteRole(@RequestParam(value = "roleId") String roleId) {
        ExecuteResult result = new ExecuteResult();
        if (StringUtils.isEmpty(roleId)) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
        if (roleService.deleteRole(roleId)) {
            return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
        } else {
            return new ExecuteResult().jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "该角色关联用户，不能删除!");
        }
    }
}
