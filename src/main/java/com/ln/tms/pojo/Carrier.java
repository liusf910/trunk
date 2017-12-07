package com.ln.tms.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tms_carrier")
public class Carrier extends BaseBean {

	@Id
	private String carrierCode;
	
	private String carrierName;

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	
	
	
	
}
