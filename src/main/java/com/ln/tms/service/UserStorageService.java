package com.ln.tms.service;

import com.ln.tms.mapper.UserStorageMapper;
import com.ln.tms.pojo.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserStorageService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class UserStorageService {

    @Autowired
    private UserStorageMapper userStorageMapper;

    /**
     * 保存用户和仓库关联信息
     *
     * @param userStorage UserStorage
     */
    @Transactional
    public void saveUserStorage(UserStorage userStorage) {
        userStorageMapper.saveUserStorage(userStorage);
    }

    /**
     * 清空用户对于的仓库
     *
     * @param userId  用户id
     */
    @Transactional
    public void clearUSByUserId(String userId) {
        userStorageMapper.clearUSByUserId(userId);
    }
}
