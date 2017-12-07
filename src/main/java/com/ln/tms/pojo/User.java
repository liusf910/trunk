package com.ln.tms.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Admin -后台管理人员
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_user")
public class User extends BaseBean {

    @Id
    private String userId;
    /**
     * 登录名
     */
    private String logonName;
    /**
     * 密码
     */
    private String logonPwd;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 所属仓库
     */
    private String storageCode;

    /**
     * 快递公司ID
     */
    private String shipperCode;

    /**
     * 快递公司名字
     */
    @Transient
    private String courierName;

    /**
     * 仓库名字
     */
    @Transient
    private String storageName;

    /**
     * 是否删除，0表示正常
     */
    private String isDelete;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 修改人ID
     */
    private String lastId;

    /**
     * 备注信息
     */
    private String comment;

    /**
     * 用户类型编号
     */
    @Transient
    private String roleType;

    /**
     * 用户角色名称
     */
    @Transient
    private String roleName;

    /**
     * 创建人姓名
     */
    @Transient
    private String createName;

    /**
     * 最后修改人姓名
     */
    @Transient
    private String lastName;

    /**
     * 操作人IP
     */
    @Transient
    private String ip;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogonName() {
        return logonName;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }

    public String getLogonPwd() {
        return logonPwd;
    }

    public void setLogonPwd(String logonPwd) {
        this.logonPwd = logonPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}