package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.RolePopedom;

import java.util.List;

/**
 * RoleMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface RolePopedomMapper extends MyMapper<RolePopedom> {

    /**
     * 批量绑定角色的权限
     *
     * @param rolePopedoms
     * @return Integer
     */
    Integer saveRolePopedomList(List<RolePopedom> rolePopedoms);

    /**
     * 根据角色编号查询出绑定的权限
     *
     * @param roleId
     * @return List
     */
    List<RolePopedom> queryByRoleId(String roleId);

    /**
     * 根据角色清除权限
     *
     * @param roleId
     * @return Integer
     */
    Integer clearRPbyRoleId(String roleId);
}
