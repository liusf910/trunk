package com.ln.tms.pojo;

import javax.persistence.Table;

/**
 * StorageCourier - 仓库快递公司关联信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_storage_courier")
public class StorageCourier extends BaseBean {
    
	/**
	 * 仓库编号
	 */    
	private String storageCode;
	
	/**
	 * 仓库名称
	 */    
	private String storageName;
	
    /**
     * 快递公司编号
     */
    private String shipperCode;
    
    /**
     * 快递公司名称
     */
    private String courierName;

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

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}
    
    
    
   
}