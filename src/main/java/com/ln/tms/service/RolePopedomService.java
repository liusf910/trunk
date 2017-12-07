package com.ln.tms.service;

import com.ln.tms.mapper.RolePopedomMapper;
import com.ln.tms.pojo.RolePopedom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * RolePopedomService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class RolePopedomService {

    @Autowired
    private RolePopedomMapper rolePopedomMapper;

    /**
     * 批量绑定角色的权限
     *
     * @param rolePopedoms 角色权限集合
     * @return Integer
     */
    @Transactional
    public Integer saveRolePopedom(List<RolePopedom> rolePopedoms) {
        return rolePopedomMapper.saveRolePopedomList(rolePopedoms);
    }

    /**
     * 根据角色编号查询出用户绑定的编号
     *
     * @param roleId 角色编号
     * @return List
     */
    @Transactional(readOnly = true)
    public List<RolePopedom> queryByRoleId(String roleId) {
        return rolePopedomMapper.queryByRoleId(roleId);
    }

    /**
     * 根据角色编号清除角色的权限
     *
     * @return Integer
     */
    @Transactional
    public Integer clearRPbyRoleId(String roleId) {
        return rolePopedomMapper.clearRPbyRoleId(roleId);
    }
}
