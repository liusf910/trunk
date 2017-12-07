package com.ln.tms.pojo;

import javax.persistence.*;

@Table(name = "tms_fileup")
public class Fileup extends BaseBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 所属模块1-0,1-1,1-2（时效、揽收、签收）
     */
    private String belongTo;

    /**
     * 创建人,用户ID
     */
    private String userId;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 客户端操作IP
     */
    private String ip;

    @Transient
    private String logonName;

    @Transient
    private String startDate;

    @Transient
    private String endDate;

    @Transient
    private boolean tookSign;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo == null ? null : belongTo.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getLogonName() {
        return logonName;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isTookSign() {
        return tookSign;
    }

    public void setTookSign(boolean tookSign) {
        this.tookSign = tookSign;
    }
}