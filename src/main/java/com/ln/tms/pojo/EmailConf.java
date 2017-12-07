package com.ln.tms.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tms_email_conf")
public class EmailConf extends BaseBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer emailId;

    /**
     * SMTP服务器地址
     */
    private String server;

    /**
     * SMTP服务器端口
     */
    private String port;

    /**
     * SMTP用户名
     */
    private String smtpName;

    /**
     * SMTP密码
     */
    private String smtpPwd;

    /**
     * SMTP是否启用SSL，1是0否
     */
    private String isSsl;


    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server == null ? null : server.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getSmtpName() {
        return smtpName;
    }

    public void setSmtpName(String smtpName) {
        this.smtpName = smtpName == null ? null : smtpName.trim();
    }

    public String getSmtpPwd() {
        return smtpPwd;
    }

    public void setSmtpPwd(String smtpPwd) {
        this.smtpPwd = smtpPwd == null ? null : smtpPwd.trim();
    }

    public String getIsSsl() {
        return isSsl;
    }

    public void setIsSsl(String isSsl) {
        this.isSsl = isSsl == null ? null : isSsl.trim();
    }

}