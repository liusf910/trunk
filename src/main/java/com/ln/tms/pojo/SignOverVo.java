package com.ln.tms.pojo;



/**
 * SignOverVo - 签收超时原因Vo
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class SignOverVo  {
	
	/**
     * 配送状态
     */
    private String orderStatus;

	/**
     * 签收超时原因
     */
    private String signReason;

    /**
     * 人工导入签收时间
     */
    private String fileSignTime;

	public String getSignReason() {
		return signReason;
	}

	public void setSignReason(String signReason) {
		this.signReason = signReason;
	}

	public String getFileSignTime() {
		return fileSignTime;
	}

	public void setFileSignTime(String fileSignTime) {
		this.fileSignTime = fileSignTime;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
   
}