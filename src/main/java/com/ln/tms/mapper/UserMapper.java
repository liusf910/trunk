package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Courier;
import com.ln.tms.pojo.Storage;
import com.ln.tms.pojo.User;
import com.ln.tms.pojo.UserStorage;

import java.util.List;

/**
 * UserMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface UserMapper extends MyMapper<User> {

    /**
     * 分页查询用户列表
     *
     * @param user
     * @return List
     */
    List<User> queryUserAll(User user);

    /**
     * 用户总条数
     *
     * @param user
     * @return Integer
     */
    Integer queryTotal(User user);

    /**
     * 逻辑删除用户
     *
     * @param id
     * @return
     */
    void deleteByIdLJ(String id);

    /**
     * 校验登录名字
     *
     * @param user
     * @return Integer
     */
    Integer checkLogonName(User user);

    /**
     * 查询用户详情
     *
     * @param userId
     * @return User
     */
    User queryUserDetail(String userId);

    /**
     * 根据用户id获取对应的仓库
     *
     * @param userId
     * @return List
     */
    List<UserStorage> getUserStorage(String userId);

    /**
     * 保存用户信息
     *
     * @param user
     */
    void saveUser(User user);

    /**
     * 根据用户id获取对应的快递公司
     *
     * @param userId
     * @return Courier
     */
    Courier getUserCourier(String userId);

    /**
     * 查询用户的权限
     *
     * @param userId
     * @return List
     */
    List<String> queryPmsByUserId(String userId);

    /**
     * 获取用户绑定的仓库
     *
     * @param userId
     * @return List
     */
    List<Storage> getBindUserStorage(String userId);
    
    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
	User queryByIdNew(String userId);

}
