package com.ln.tms.pojo;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Storage - 仓库信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_storage")
public class Storage extends BaseBean {
    
	/**
	 * 仓库编号
	 */    
	private String storageCode;
	
    /**
     * 仓库名称
     */
    private String storageName;
    
    /**
     * 拥有快递公司
     */
	@Transient
    private String courierName;
   
    /**
     * 仓库快递公司
     */
	@Transient
    private String storageCourier;
    
    /**
     * 判断是否选中之用
     */
    @Transient
    private Boolean isSelected = false;
    
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
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
	public String getStorageCourier() {
		return storageCourier;
	}
	public void setStorageCourier(String storageCourier) {
		this.storageCourier = storageCourier;
	}
	public Boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
    
   
}