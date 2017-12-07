package com.ln.tms.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.StateType;
import com.ln.tms.enums.TookType;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;

/**
 * InfoMonito - 监测管理（圆通）
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class InfoMonito extends BaseBean {
	
	/**
	 * info_id
	 */
	public String infoId; 
	
	/**
	 * 发货日期
	 */
	public Date shipmentsRq;

	/**
	 * 外部单号
	 */
	public String extOrderCode;

	/**
	 * 网络单号
	 */
	public String orderCode;

	/**
	 * 收件联系人
	 */
	public String linkman;

	/**
	 * 收件人省
	 */
	public String province;

	/**
	 * 收件人市
	 */
	public String city;

	/**
	 * 收件人城市分级
	 */
	public String cityScale;

	/**
	 * 收件人详细地址
	 */
	public String addr;

	/**
	 * 收件人联系电话
	 */
	public String phone;

	/**
	 * 快递公司名称
	 */
	public String courierName;

	/**
	 * 速递单号
	 */
	public String logisticCode;

	/**
	 * 数量
	 */
	public String number;

	/**
	 * 重量
	 */
	public String weight;

	/**
	 * 发货仓库代码
	 */
	public String storageCode;
	
	/**
	 * 发货仓库
	 */
	public String storage;


	/**
	 * 发货时间
	 */
	public Date shipmentsTime;
	
	/**
	 * 付款时间
	 */
	public Date payTime;

	/**
	 * 快递员揽件日期(系统)
	 */
	public Date xtTookTime;

	/**
	 * 快递员揽件日期（人工）
	 */
	public Date rgTookTime;

	/**
	 * 揽收是否超时
	 */
	public String tookOut;

	/**
	 * 人工导入揽收延迟原因
	 */
	public String tookOutReason;
    
	//TD
	/**
	 * 人工导入揽收时间标记
	 */
	public String tookFlag;

	/**
	 * 快递公司时限
	 */
	public Integer validityDay;

	/**
	 * 计划到货日期
	 */
	public Date planTime;

	/**
	 * 系统对接签收日期
	 */
	public Date xtSignTime;

	/**
	 * 人工导入签收日期
	 */
	public Date rgSignTime;
   
	//TD
	/**
	 * 判别列
	 */
	public String pbLine;

	/**
	 * 超时天数
	 */
	public Integer overDay;

	/**
	 * 到货天数（在途天数）
	 */
	public Integer attritDay;

	/**
	 * 配送状态
	 */
	public String orderState;
    
	//TD
	/**
	 * 快递异常备注
	 */
	public String reason;

	/**
	 * 签收人
	 */
	public String signUser;
   
	//TD
	/**
	 * 快递状态
	 */
	public String state;
	
	/**
	 * 快递备注运作状态
	 */
	public String beiuzhuState;
    
	//TD
	/**
	 * 延迟第一天快递流转信息
	 */
	public String yckdlzxxOne;
    
	//TD
	/**
	 * 延迟第二天快递流转信息
	 */
	public String yckdlzxxTwo;
    
	//TD
	/**
	 * 延迟第三天快递流转信息
	 */
	public String yckdlzxxThree;
	
	//TD
	/**
	 * 当前快递流转信息
	 */
	public String kdlzxx;

	//TD
	/**
	 * 人工导入早于系统对接24h 标记
	 */
	public String rgxtFlag;

	//TD
	/**
	 * 20点后发货当天揽收 标记
	 */
	public String fhTookFlag;

	/**
	 * 轨迹信息json格式
	 */
	public String infoTrace;
	

	public Date getShipmentsRq() {
		return shipmentsRq;
	}

	public void setShipmentsRq(Date shipmentsRq) {
		this.shipmentsRq = shipmentsRq;
	}

	public String getExtOrderCode() {
		return extOrderCode;
	}

	public void setExtOrderCode(String extOrderCode) {
		this.extOrderCode = extOrderCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city == null ? null : city.trim();
	}

	public String getCityScale() {
		return cityScale;
	}

	public void setCityScale(String cityScale) {
		this.cityScale = cityScale;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr == null ? null : addr.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getLogisticCode() {
		return logisticCode;
	}

	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode == null ? null : logisticCode.trim();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public Date getShipmentsTime() {
		return shipmentsTime;
	}

	public void setShipmentsTime(Date shipmentsTime) {
		this.shipmentsTime = shipmentsTime;
	}

	public Date getXtTookTime() {
		return xtTookTime;
	}

	public void setXtTookTime(Date xtTookTime) {
		this.xtTookTime = xtTookTime;
	}

	public Date getRgTookTime() {
		return rgTookTime;
	}

	public void setRgTookTime(Date rgTookTime) {
		this.rgTookTime = rgTookTime;
	}

	public String getTookOut() {
		String tookOut="是";
		SimpleDateFormat sdf=new SimpleDateFormat("HH");    
		int hour=Integer.parseInt(sdf.format(this.getShipmentsTime()));
		if(this.getShipmentsTime()!=null && this.getXtTookTime()!=null){
			if(hour<=20){
				String shipRq=new SimpleDateFormat("yyyy-MM-dd").format(this.getShipmentsTime());
				String xtTookRq=new SimpleDateFormat("yyyy-MM-dd").format(this.getXtTookTime());
				if(shipRq.equals(xtTookRq)){
					tookOut="否";
				}
			}else{
				long hours=DateUtils.getDateHours(this.getXtTookTime(),this.getShipmentsTime());
				if(hours<=24){
					tookOut="否";
				}
			}
		}
		return tookOut;
	}

	public void setTookOut(String tookOut) {
		this.tookOut = tookOut;
	}

	public String getTookOutReason() {
		if (StringUtils.isNotBlank(tookOutReason)) {
            return (TookType.codeOf(Integer.parseInt(tookOutReason))).getDesc();
        }
    	return tookOutReason;
	}

	public void setTookOutReason(String tookOutReason) {
		this.tookOutReason = tookOutReason;
	}

	public String getTookFlag() {
		if(this.getRgTookTime()!=null){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getRgTookTime());
		}
		return tookFlag;
	}

	public void setTookFlag(String tookFlag) {
		this.tookFlag = tookFlag;
	}

	public Integer getValidityDay() {
		return validityDay;
	}

	public void setValidityDay(Integer validityDay) {
		this.validityDay = validityDay;
	}

	public Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	public Date getXtSignTime() {
		return xtSignTime;
	}

	public void setXtSignTime(Date xtSignTime) {
		this.xtSignTime = xtSignTime;
	}

	public Date getRgSignTime() {
		return rgSignTime;
	}

	public void setRgSignTime(Date rgSignTime) {
		this.rgSignTime = rgSignTime;
	}

	public String getPbLine() {
		if(this.getXtSignTime()!=null && this.getRgSignTime()!=null){
			long hours=DateUtils.getDateHours(this.getXtSignTime(), this.getRgSignTime());
			if(hours>24){
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getXtSignTime());
			}
		}else if(this.getXtSignTime()==null && this.getRgSignTime()!=null){
			return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getRgSignTime());
		}
		return pbLine;
	}

	public void setPbLine(String pbLine) {
		this.pbLine = pbLine;
	}

	public Integer getOverDay() {
		return overDay;
	}

	public void setOverDay(Integer overDay) {
		this.overDay = overDay;
	}

	public Integer getAttritDay() {
		return attritDay;
	}

	public void setAttritDay(Integer attritDay) {
		this.attritDay = attritDay;
	}

	public String getOrderState() {
		if(StringUtils.isNotBlank(orderState)){
    		return (OrderType.codeOf(Integer.parseInt(orderState))).getDesc();
    	}
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getReason() {
		if (StringUtils.isNotBlank(reason)) {
            return (SignType.codeOf(Integer.parseInt(reason))).getDesc();
        }
	    return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSignUser() {
		return signUser;
	}

	public void setSignUser(String signUser) {
		this.signUser = signUser;
	}

	public String getState() {
    	return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public String getBeiuzhuState() {
		String beizhu="";
		if(StringUtils.isNotBlank(this.getState())){
			beizhu =(StateType.codeOf(Integer.parseInt(this.getState()))).getDescribe();
    	}
		return beizhu;
	}

	public String getKdlzxx() {
		/*String kdlzxx="";
		if(StringUtils.isBlank(this.getOverDay()+"")||this.getOverDay()==0 ){
			List<Trace> list=JsonUtils.toObject(this.getInfoTrace(), new TypeReference<List<Trace>>() {});
			kdlzxx=list.get(list.size()-1).getAcceptStation();
		}
		return kdlzxx;*/
		if(StringUtils.isNotBlank(this.getInfoTrace())){
			List<Trace> list=JsonUtils.toObject(this.getInfoTrace(), new TypeReference<List<Trace>>() {});
			return list.get(list.size()-1).getAcceptStation();
		}
		return kdlzxx;
	}

	public String getRgxtFlag() {
		if(this.getXtSignTime()!=null && this.getRgSignTime()!=null){
			long hours=DateUtils.getDateHours(this.getXtSignTime(), this.getRgSignTime());
			if(hours>24){
				return "是";
			}
		}
		return "否";
	}

	public void setRgxtFlag(String rgxtFlag) {
		this.rgxtFlag = rgxtFlag;
	}

	public String getFhTookFlag() {
		if(this.getShipmentsTime()!=null){
			int hour=Integer.parseInt(new SimpleDateFormat("HH").format(this.getShipmentsTime()));
			if(hour>20 && this.getXtTookTime()!=null){
				String shipRq=new SimpleDateFormat("yyyy-MM-dd").format(this.getShipmentsTime());
				String xtTookRq=new SimpleDateFormat("yyyy-MM-dd").format(this.getXtTookTime());
				if(shipRq.equals(xtTookRq)){
					 return "是";
				}
			}
		}   
		return "否";
	}

	public void setFhTookFlag(String fhTookFlag) {
		this.fhTookFlag = fhTookFlag;
	}

	public String getInfoTrace() {
		return infoTrace;
	}

	public void setInfoTrace(String infoTrace) {
		this.infoTrace = infoTrace;
	}

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	
}