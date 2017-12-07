package com.ln.tms.pojo;

import java.util.List;
import java.util.Map;

public class MailModel {
   
	/**
     * 邮箱Host
     */
    private String host;
    
    /**
     * 邮箱用户名
     */
    private String userName;
    
    /**
     * 邮箱密码
     */
    private String password;
    
    /**
     * 发件人邮箱
     */
    private String fromEmail;
    
    /**
     * 收件人邮箱，多个邮箱以“,”分隔
     */
    private String toEmails;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件中的图片，为空时无图片。map中的key为图片名（带图片后缀），value为图片地址
     */
    private List<Map<String, String>> pictures;
    /**
     * 邮件中的附件，为空时无附件。map中的key为附件名（带文件后缀），value为附件地址
     */
    private List<Map<String, String>> attachments;
    
	public String getToEmails() {
		return toEmails;
	}
	public void setToEmails(String toEmails) {
		this.toEmails = toEmails;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<Map<String, String>> getPictures() {
		return pictures;
	}
	public void setPictures(List<Map<String, String>> pictures) {
		this.pictures = pictures;
	}
	public List<Map<String, String>> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Map<String, String>> attachments) {
		this.attachments = attachments;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	
}

