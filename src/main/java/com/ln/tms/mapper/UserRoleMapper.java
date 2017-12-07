package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.UserRole;

import java.util.List;

/**
 * RoleMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface UserRoleMapper extends MyMapper<UserRole> {
    /**
     * 根据角色编号查询出是否有用户绑定
     *
     * @param roleId
     * @return List
     */
    List<UserRole> bingUserByRoleId(String roleId);

    /**
     * 根据用户编号查询出绑定的角色
     *
     * @param userId
     * @return List
     */
    List<UserRole> bingRoleByUserId(String userId);

    /**
     * 插入用户角色
     *
     * @param userRole
     * @return Integer
     */
    Integer saveUserRole(UserRole userRole);

    /**
     * 清除用户的角色信息
     *
     * @param userId
     * @return Integer
     */
    Integer clearURbyUserId(String userId);
}
