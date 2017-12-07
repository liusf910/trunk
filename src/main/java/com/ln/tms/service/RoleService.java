package com.ln.tms.service;

import com.github.pagehelper.PageInfo;
import com.ln.tms.pojo.Permission;
import com.ln.tms.pojo.Role;
import com.ln.tms.pojo.RolePopedom;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RoleService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class RoleService extends BaseService<Role> {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePopedomService rolePopedomService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页查询快递列表
     *
     * @param role 查询条件
     * @param page 页
     * @param row  条数
     * @return PageInfo
     */
    @Transactional(readOnly = true)
    public PageInfo<Role> queryRolePage(Role role, Integer page, Integer row) {
        Example example = new Example(Role.class);
        example.setOrderByClause("create_date DESC");
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(role.getRoleName())) {
            criteria.andLike("roleName", "%" + role.getRoleName() + "%");
        }
        return super.queryPageListByExample(example, page, row);
    }

    /**
     * 新增角色，绑定权限
     *
     * @param role  角色数据
     * @param menus 权限菜单
     * @return boolean
     */
    @Transactional
    public boolean saveRole(Role role, String menus) {
        Assert.notNull(role);
        String roleId = TmsUtils.getUUID();
        role.setRoleId(roleId);
        super.saveSelective(role);
        if (StringUtils.isNoneBlank(menus)) {
            String[] split = StringUtils.split(menus, ",");
            List<RolePopedom> rolePopedoms = new ArrayList<RolePopedom>();
            for (String mid : split) {
                RolePopedom rp = new RolePopedom();
                rp.setMid(mid);
                rp.setPid(permissionService.queryById(mid).getPid());
                rp.setRoleId(roleId);
                rolePopedoms.add(rp);
            }
            if (CollectionUtils.isNotEmpty(rolePopedoms)) {
                rolePopedomService.saveRolePopedom(rolePopedoms);
            }
        }
        return true;
    }

    /**
     * 查询角色绑定的权限
     *
     * @param roleId 角色编号
     * @return Map
     */
    @Transactional(readOnly = true)
    public Map<String, Object> RoleBingPermission(String roleId) {
        Assert.notNull(roleId);

        Map<String, Object> resultMap = new HashMap<>();

        List<Permission> permissions = permissionService.queryAll();
        List<RolePopedom> rolePopedoms = rolePopedomService.queryByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(rolePopedoms)) {
            List<Permission> lists = new ArrayList<Permission>();
            List<String> menus = new ArrayList<String>();
            for (Permission permission : permissions) {
                for (RolePopedom popedom : rolePopedoms) {
                    if (popedom.getMid().equals(permission.getMid())) {
                        permission.setIsChecked(true);
                        menus.add(permission.getMid());
                    }
                }
                lists.add(permission);
            }
            resultMap.put("lists", lists);
            resultMap.put("menus", StringUtils.join(menus, ","));
        } else {
            resultMap.put("lists", permissions);
            resultMap.put("menus", "");
        }
        return resultMap;
    }

    /**
     * 编辑角色，绑定权限
     *
     * @param role  角色数据
     * @param menus 权限菜单
     * @return boolean
     */
    @Transactional
    public boolean editRole(Role role, String menus) {
        Assert.notNull(role);
        super.updateSelective(role);
        rolePopedomService.clearRPbyRoleId(role.getRoleId());

        if (StringUtils.isNotBlank(menus)) {
            String[] split = StringUtils.split(menus, ",");
            List<RolePopedom> rolePopedoms = new ArrayList<RolePopedom>();
            for (String mid : split) {
                RolePopedom rp = new RolePopedom();
                rp.setMid(mid);
                rp.setPid(permissionService.queryById(mid).getPid());
                rp.setRoleId(role.getRoleId());
                rolePopedoms.add(rp);
            }
            if (CollectionUtils.isNotEmpty(rolePopedoms)) {
                rolePopedomService.saveRolePopedom(rolePopedoms);
            }
        }
        return true;
    }

    /**
     * 删除角色
     *
     * @param roleId 角色编号
     * @return Boolean
     */
    @Transactional
    public Boolean deleteRole(String roleId) {
        //查询用户是否绑定其角色，有的话不能删除
        if (CollectionUtils.isNotEmpty(userRoleService.bingUserByRoleId(roleId))) {
            return false;
        }
        //用户没有绑定的话则删除角色权限关联
        rolePopedomService.clearRPbyRoleId(roleId);
        //删除角色
        super.deleteById(roleId);
        return true;
    }

}
