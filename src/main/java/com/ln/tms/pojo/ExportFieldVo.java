package com.ln.tms.pojo;


/**
 * ExportFieldVo - 可导出字段Vo
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class ExportFieldVo extends BaseBean {

	/**
     * 导出名称列表（中文）
     */
    private String expName;

    /**
     * 导出字段名称（字段名）
     */
    private String expFieldName;
    
    /**
     * 是否选择过 (1:选择；0：未选择)
     */
    private String isSelect;

	public String getExpName() {
		return expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	public String getExpFieldName() {
		return expFieldName;
	}

	public void setExpFieldName(String expFieldName) {
		this.expFieldName = expFieldName;
	}

	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

	
    
    
    

}