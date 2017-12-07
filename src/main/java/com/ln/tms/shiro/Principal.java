package com.ln.tms.shiro;

import com.ln.tms.pojo.User;

/**
 * Principal
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class Principal {

    /**
     * ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户
     */
    private User user;

    /**
     * 构造方法
     *
     * @param id       ID
     * @param username 用户名
     */
    public Principal(String id, String username, User user) {
        this.id = id;
        this.username = username;
        this.user = user;
    }

    /**
     * 获取ID
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 重写toString方法
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return username;
    }

}