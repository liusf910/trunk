package com.ln.tms.controller;



import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.MailModel;
import com.ln.tms.service.SendMailService;

/**
 * SendEmailController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/mail")
public class SendMailController extends BaseController {
    
	@Autowired
    private SendMailService sendMailService;
	
	/**
	 * 发送邮件
	 * @param mailModel 邮件Vo
	 * @return
	 */
	@RequestMapping(value = "send")
	@ResponseBody
	public Map<String, Object> sendMail(MailModel mailModel) {
		/* 测试示例
		//163邮箱
		mailModel.setHost("smtp.163.com");
		mailModel.setUserName("13665518731@163.com");
		mailModel.setPassword("wusheng12");
		//qq邮箱
		mailModel.setHost("smtp.qq.com");
		mailModel.setUserName("474015864@qq.com");
		mailModel.setPassword("kddyibkppetsbhbf");
		//发件人邮箱
		//mailModel.setFromEmail("13665518731@163.com");
		mailModel.setFromEmail("474015864@qq.com");
		//收件人邮箱
		mailModel.setToEmails("474015864@qq.com,1292964974@qq.com");
		//邮件主题
		mailModel.setSubject("邮件主题");
		//邮件内容
		mailModel.setContent("邮件内容");
		//邮件附件
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		Map<String, String> filemap=new HashMap<String, String>();
		filemap.put("kdjc20170412105920.csv", "/downFile/csv/kdjc20170412105920.csv");
		Map<String, String> filemap2=new HashMap<String, String>();
		filemap2.put("kdjc20170412110021.csv", "/downFile/csv/kdjc20170412110021.csv");
		list.add(filemap);
		list.add(filemap2);
		mailModel.setAttachments(list);*/
		
		ExecuteResult result = new ExecuteResult();
		String resultStr=sendMailService.sendMail(mailModel);
		if("发送成功".equals(resultStr)){
			return result.jsonReturn(HttpStatus.SC_OK,resultStr);
		}
		return result.jsonReturn(HttpStatus.SC_MOVED_PERMANENTLY, resultStr);
	}
	
	
}
