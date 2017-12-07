package com.ln.tms.pojo;

import javax.persistence.Table;

@Table(name = "tms_export_fields")
public class ExportFields extends BaseBean {
    /**
     * 所属模块（如1-0，1-1，2-1等）
     */
    private String belongTo;

    /**
     * 导出名称列表（中文）用“#”分开
     */
    private String expNameList;

    /**
     * 导出字段名称（字段名）用“#”分开
     */
    private String expFieldName;

    /**
     * 未导出名称列表（中文）用“#”分开
     */
    private String noexpNameList;

    /**
     * 未导出字段名称（字段名）用“#”分开
     */
    private String noexpFieldName;
    
    /**
     * 用户id
     */
    private String userId;


    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo == null ? null : belongTo.trim();
    }

    public String getExpNameList() {
        return expNameList;
    }

    public void setExpNameList(String expNameList) {
        this.expNameList = expNameList == null ? null : expNameList.trim();
    }

    public String getExpFieldName() {
        return expFieldName;
    }

    public void setExpFieldName(String expFieldName) {
        this.expFieldName = expFieldName == null ? null : expFieldName.trim();
    }

    public String getNoexpNameList() {
        return noexpNameList;
    }

    public void setNoexpNameList(String noexpNameList) {
        this.noexpNameList = noexpNameList == null ? null : noexpNameList.trim();
    }

    public String getNoexpFieldName() {
        return noexpFieldName;
    }

    public void setNoexpFieldName(String noexpFieldName) {
        this.noexpFieldName = noexpFieldName == null ? null : noexpFieldName.trim();
    }
}