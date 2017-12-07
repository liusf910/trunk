package com.ln.tms.mapper;

import com.ln.tms.pojo.UserStorage;
import com.ln.tms.mymapper.MyMapper;

/**
 * UserStorageMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface UserStorageMapper extends MyMapper<UserStorage> {
    
	/**
	 * 保存用户和仓库关联信息
	 * @param userStorage
	 */
	void saveUserStorage(UserStorage userStorage);
    
	/**
	 * 清空用户对于的仓库
	 * @param userId
	 */
	void clearUSByUserId(String userId);
    
}
