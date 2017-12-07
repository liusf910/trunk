package com.ln.tms.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ln.tms.mapper.StorageMapper;
import com.ln.tms.mapper.UserMapper;
import com.ln.tms.pojo.Courier;
import com.ln.tms.pojo.Role;
import com.ln.tms.pojo.Storage;
import com.ln.tms.pojo.StorageCourier;
import com.ln.tms.pojo.User;
import com.ln.tms.pojo.UserRole;
import com.ln.tms.pojo.UserStorage;
import com.ln.tms.util.TmsUtils;

/**
 * UserService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class UserService extends BaseService<User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserStorageService userStorageService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CourierService courierService;

    /**
     * 校验登录名字
     *
     * @param userId    用户编号
     * @param logonName 登录名
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean checkLogonName(String userId, String logonName) {
        User user = new User();
        user.setUserId(userId);
        user.setLogonName(logonName);
        return userMapper.checkLogonName(user) > 0 ;
    }

    /**
     * 条件查询用户集合
     *
     * @param user 查询条件
     * @return List
     */
    @Transactional(readOnly = true)
    public List<User> queryUserList(User user) {
        return userMapper.queryUserAll(user);
    }

    /**
     * 条件查询用户条数
     *
     * @param user 查询条件
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer queryTotal(User user) {
        return userMapper.queryTotal(user);
    }

    /**
     * 逻辑删除用户
     *
     * @param id 用户编号
     */
    @Transactional
    public void deleteByIdLJ(String id) {
        userMapper.deleteByIdLJ(id);
    }

    /**
     * 新增用户绑定角色
     *
     * @param user 用户数据
     * @return boolean
     */
    @Transactional
    public boolean saveUser(User user) {
        Assert.notNull(user);

        user.setUserId(TmsUtils.getUUID());
        user.setIsDelete("0");
        userMapper.saveUser(user);

        String roleType = user.getRoleType();
        if (StringUtils.isNotBlank(roleType)) {
            String[] split = StringUtils.split(roleType, ",");
            for (String roleId : split) {
                userRoleService.saveUserRole(new UserRole(user.getUserId(),
                        roleId));
            }
        }

        //保存用户仓库快递关联信息
        String shipperCode=user.getShipperCode();
        if(StringUtils.isNotBlank(shipperCode)){
        	String[] storageCodeShipperCodeArr=shipperCode.split(",");
        	for (String storageCodeShipperCode : storageCodeShipperCodeArr){
        		String[] shipperCodeArr=storageCodeShipperCode.split("-");        			
        		userStorageService.saveUserStorage(new UserStorage(user.getUserId(), shipperCodeArr[0],shipperCodeArr[1]));
        	}
        }
        return true;
    }

    /**
     * 查询用户绑定的角色
     *
     * @param userId 用户编号
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Role> userBingRole(String userId) {
        List<Role> result = new ArrayList<Role>();

        List<Role> roles = roleService.queryAll();
        List<UserRole> selectedRoles = userRoleService.bingRoleByUserId(userId);
        for (Role role : roles) {
            for (UserRole userRole : selectedRoles) {
                if (role.getRoleId().equals(userRole.getRoleId())) {
                    role.setIsSelected(true);
                }
            }
            result.add(role);
        }
        return result;
    }

    /**
     * 修改用户绑定角色
     *
     * @param user 用户数据
     * @return boolean
     */
    @Transactional
    public boolean editUser(User user) {
        Assert.notNull(user);
        super.updateSelective(user);
        userRoleService.clearURbyUserId(user.getUserId());
        userStorageService.clearUSByUserId(user.getUserId());
        String roleType = user.getRoleType();
        if (StringUtils.isNotBlank(roleType)) {
            String[] split = StringUtils.split(roleType, ",");
            for (String roleId : split) {
                userRoleService.saveUserRole(new UserRole(user.getUserId(),
                        roleId));
            }
        }

        //保存用户仓库快递关联信息
        String shipperCode=user.getShipperCode();
        if(StringUtils.isNotBlank(shipperCode)){
        	String[] storageCodeShipperCodeArr=shipperCode.split(",");
        	for (String storageCodeShipperCode : storageCodeShipperCodeArr){
        		String[] shipperCodeArr=storageCodeShipperCode.split("-");        			
        		userStorageService.saveUserStorage(new UserStorage(user.getUserId(), shipperCodeArr[0],shipperCodeArr[1]));
        	}
        }
        return true;
    }

    /**
     * 查询用户详情
     *
     * @param userId 用户编号
     * @return User
     */
    @Transactional(readOnly = true)
    public User queryUserDetail(String userId) {
        Assert.notNull(userId);
        return userMapper.queryUserDetail(userId);
    }

    /**
     * 查询用户的权限
     *
     * @param userId 用户编号
     * @return List
     */
    @Transactional(readOnly = true)
    public List<String> queryPmsByUserId(String userId) {
        return userMapper.queryPmsByUserId(userId);
    }

    /**
     * 根据用户id获取对应的仓库
     *
     * @param userId 用户编号
     * @return List
     */
    @Transactional
    public List<Storage> getUserStorage(String userId) {
        List<Storage> sList = storageService.queryAllNew();
        List<UserStorage> usList = userMapper.getUserStorage(userId);
        for (UserStorage us : usList) {
            for (Storage s : sList) {
                if (us.getStorageCode().equals(s.getStorageCode())) {
                    s.setIsSelected(true);
                }
            }
        }
        return sList;
    }

    /**
     * 根据用户id获取对应的快递公司
     *
     * @param userId 用户编号
     * @return List
     */
    @Transactional
    public List<Courier> getUserCourier(String userId) {
        List<Courier> xzList = new ArrayList<Courier>();
        Courier userCourier = userMapper.getUserCourier(userId); // 用户对应的快递公司
        List<UserStorage> usList = userMapper.getUserStorage(userId); // 用户对应的仓库信息
        if (usList != null) {
            for (UserStorage us : usList) { // 遍历仓库信息获取展示的快递公司
                List<StorageCourier> scList = storageMapper
                        .getStorageCouriers(us.getStorageCode());
                for (StorageCourier sc : scList) {
                    Courier courier = courierService.getCourierById(sc
                            .getShipperCode());
                    if (courier != null) {
                        xzList.add(courier);
                    }
                }
            }
        }
        // xzList不为空且长度大于2去重
        if (xzList != null && xzList.size() >= 2) {
            List<String> listTemp = new ArrayList<String>();
            Iterator<Courier> it = xzList.iterator();
            while (it.hasNext()) {
                String a = it.next().getShipperCode();
                if (listTemp.contains(a)) {
                    it.remove();
                } else {
                    listTemp.add(a);
                }
            }
        }
        if (userCourier != null && xzList != null) {
            for (Courier c : xzList) { // 设置选中项
                if (c.getShipperCode().equals(userCourier.getShipperCode())) {
                    c.setIsSelected(true);
                }
            }
        }
        return xzList;
    }

    /**
     * 获取用户绑定的仓库
     *
     * @param userId 用户编号
     * @return List
     */
    @Transactional
    public List<Storage> getBindUserStorage(String userId) {
        return userMapper.getBindUserStorage(userId);
    }
  
    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
	public User queryByIdNew(String userId) {
		return userMapper.queryByIdNew(userId);
	}

}
