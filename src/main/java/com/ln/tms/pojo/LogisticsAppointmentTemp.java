package com.ln.tms.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

public class LogisticsAppointmentTemp {
	/**
     * 80或180单号
     */
	private String oddNum;
	
	/**
     * 品项
     */
	private String item;
	
	/**
     * 数量
     */
	private Integer number;
	
	/**
     * 件数
     */
	private Integer numberPackage;
	
	/**
     * 预约送达日期
     */
	private String dateAppoint;
	
	/**
     * 发货方
     */
	private String shipper;
	
	/**
     * 发货城市
     */
	private String shipperCity;
	
	/**
     * 承运商
     */
	private String carrier;
	
	/**
     * 预约人
     */
	private String reservatePerson;
	
	/**
     * 预约人所属公司
     */
	private String appointCompany;
	
	/**
     * 预约人联系方式
     */
	private String phone;
	
	/**
     * 仓库
     */
	private String warehouse;
	
	/**
     * 备注
     */
	private String remark;
	

	public String getOddNum() {
		return oddNum;
	}

	public void setOddNum(String oddNum) {
		this.oddNum = oddNum;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getNumberPackage() {
		return numberPackage;
	}

	public void setNumberPackage(Integer numberPackage) {
		this.numberPackage = numberPackage;
	}

	public String getDateAppoint() {
		return dateAppoint;
	}

	public void setDateAppoint(String dateAppoint) {
		this.dateAppoint = dateAppoint;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getShipperCity() {
		return shipperCity;
	}

	public void setShipperCity(String shipperCity) {
		this.shipperCity = shipperCity;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getReservatePerson() {
		return reservatePerson;
	}

	public void setReservatePerson(String reservatePerson) {
		this.reservatePerson = reservatePerson;
	}

	public String getAppointCompany() {
		return appointCompany;
	}

	public void setAppointCompany(String appointCompany) {
		this.appointCompany = appointCompany;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
