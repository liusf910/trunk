package com.ln.tms.service;

import com.ln.tms.mapper.UserRoleMapper;
import com.ln.tms.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserRoleService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 根据角色编号查询出用户绑定的编号
     *
     * @param roleId 角色编号
     * @return List
     */
    @Transactional(readOnly = true)
    public List<UserRole> bingUserByRoleId(String roleId) {
        return userRoleMapper.bingUserByRoleId(roleId);
    }

    /**
     * 根据用户编号查询出绑定的角色
     *
     * @param userId 用户编号
     * @return List
     */
    @Transactional(readOnly = true)
    public List<UserRole> bingRoleByUserId(String userId) {
        return userRoleMapper.bingRoleByUserId(userId);
    }

    /**
     * 添加用户角色
     *
     * @param userRole 用户角色信息
     * @return Integer
     */
    @Transactional
    public Integer saveUserRole(UserRole userRole) {
        return userRoleMapper.saveUserRole(userRole);
    }

    /**
     * 清除用户的角色信息
     *
     * @param userId 用户编号
     * @return Integer
     */
    @Transactional
    public Integer clearURbyUserId(String userId) {
        return userRoleMapper.clearURbyUserId(userId);
    }
}
