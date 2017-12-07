package com.ln.tms.pojo;

import javax.persistence.Table;

/**
 * UserStorage - 用户仓库关联信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_user_storage")
public class UserStorage extends BaseBean {
	
	/**
	 * 用户id
	 */
	private String userId;
    
	/**
	 * 仓库编号
	 */    
	private String storageCode;
	
	/**
	 * 快递编号
	 */    
	private String shipperCode;
	

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
     
	public UserStorage(String userId, String storageCode) {
        this.userId = userId;
        this.storageCode = storageCode;
    }
	
	public UserStorage(String userId, String storageCode,String shipperCode) {
        this.userId = userId;
        this.storageCode = storageCode;
        this.shipperCode = shipperCode;
    }

	public String getShipperCode() {
		return shipperCode;
	}

	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}
	
    
    
    
   
}