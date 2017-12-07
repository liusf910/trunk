package com.ln.tms.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "tms_permission")
public class Permission extends BaseBean {
    /**
     * 权限
     */
    @Id
    private String mid;

    /**
     * 父级权限
     */
    private String pid;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限访问路径url
     */
    private String url;

    /**
     * 权限显示图表
     */
    private String icon;

    /**
     * 权限url访问目标区域
     */
    private String target;

    /**
     * 权限是否是菜单
     */
    private Boolean ismenu;

    /**
     * 权限是否父级权限
     */
    private Boolean isparent;

    @Transient
    private Boolean isChecked = false;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    public Boolean getIsmenu() {
        return ismenu;
    }

    public void setIsmenu(Boolean ismenu) {
        this.ismenu = ismenu;
    }

    public Boolean getIsparent() {
        return isparent;
    }

    public void setIsparent(Boolean isparent) {
        this.isparent = isparent;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Permission() {
    }

    public Permission(String url) {
        this.url = url;
    }
}