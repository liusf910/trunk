package com.ln.tms.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Storage - 仓库信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Table(name = "tms_storage_appoint")
public class StorageAppoint extends BaseBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private Integer appointLimit;
	
	private String warehouseCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAppointLimit() {
		return appointLimit;
	}

	public void setAppointLimit(Integer appointLimit) {
		this.appointLimit = appointLimit;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

}
