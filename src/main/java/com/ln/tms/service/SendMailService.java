package com.ln.tms.service;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ln.tms.pojo.MailModel;

/**
 * SendMailService 发邮件Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class SendMailService  {
    
	/**
	 * 发送邮件
	 * @param   mailModel   邮件Vo
	 * @return  String  	发送结果
	 */
	public String sendMail(MailModel mailModel) {
		// JavaMailSenderImpl
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
	    Properties prop = new Properties();
	    prop.setProperty("mail.smtp.auth", "true");
	    senderImpl.setJavaMailProperties(prop);
	    if(StringUtils.isBlank(mailModel.getHost())){
	    	return "邮箱的Host不能为空";
	    }
	    senderImpl.setHost(mailModel.getHost());
	    if("smtp.qq.com".equals(mailModel.getHost())){
	    	senderImpl.setPort(587);
	    }
	    if(StringUtils.isBlank(mailModel.getUserName())){
	    	return "邮箱的用户名不能为空";
	    }
	    senderImpl.setUsername(mailModel.getUserName());
	    if(StringUtils.isBlank(mailModel.getPassword())){
	    	return "邮箱的密码不能为空";
	    }
	    senderImpl.setPassword(mailModel.getPassword());     
	    
	    //带附件的邮件
	    MimeMessage mimeMessage = senderImpl.createMimeMessage();
	    MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true);
			if(StringUtils.isBlank(mailModel.getFromEmail())){
				return "发件人邮箱不能为空";	
			}
			helper.setFrom(mailModel.getFromEmail());
			if(StringUtils.isBlank(mailModel.getToEmails())){
				return "收件人邮箱不能为空";
			}
			String [] address = mailModel.getToEmails().split(",");
			helper.setTo(address);
			if(StringUtils.isBlank(mailModel.getSubject())){
				return "邮件主题不能为空";
			}
			helper.setSubject(mailModel.getSubject());
			helper.setText(mailModel.getContent());
			// 添加图片
            if (null != mailModel.getPictures() && mailModel.getPictures().size()>0 ) {
            	for(Map<String, String> map:mailModel.getPictures()){
            		for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();) {
            			Map.Entry<String, String> entry = it.next();
            			String cid = entry.getKey();
            			String filePath = entry.getValue();
            			if (null == cid || null == filePath) {
            				return "请确认每张图片的ID和图片地址是否齐全";
            			}
            			
            			File file = new File(filePath);
            			if (!file.exists()) {
            				return "图片" + filePath + "不存在";
            			}
            			
            			FileSystemResource img = new FileSystemResource(file);
            			helper.addInline(cid, img);
            		}
            	}
            }
            // 添加附件
            if (null != mailModel.getAttachments() && mailModel.getAttachments().size()>0) {
            	for(Map<String, String> map:mailModel.getAttachments()){
            		for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();) {
            			Map.Entry<String, String> entry = it.next();
            			String cid = entry.getKey();
            			String filePath = entry.getValue();
            			if (null == cid || null == filePath) {
            				return "请确认每个附件的ID和地址是否齐全";
            			}
            			
            			File file = new File(filePath);
            			if (!file.exists()) {
            				return "附件" + filePath + "不存在";
            			}
            			
            			FileSystemResource fileResource = new FileSystemResource(file);
            			helper.addAttachment(cid, fileResource);
            		}
            	}
            }
            helper.setSentDate(new Date());
            // 发送邮件
			senderImpl.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			return "发送失败";
		}
		return "发送成功";
	}
    


}
