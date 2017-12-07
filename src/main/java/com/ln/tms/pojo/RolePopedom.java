package com.ln.tms.pojo;

import javax.persistence.Table;

@Table(name = "tms_role_popedom")
public class RolePopedom {
    
    /**
     * 角色编号
     */
    private String roleId;

    /**
     * 权限
     */
    private String mid;

    /**
     * 父级权限
     */
    private String pid;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }


}