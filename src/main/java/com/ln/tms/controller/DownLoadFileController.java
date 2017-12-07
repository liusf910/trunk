package com.ln.tms.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * DownLoadFileController - 文件下载
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/file")
public class DownLoadFileController {
	
	/**
	 * 文件存放磁盘
	 */
	@Value("${file.disk.path}")
	private String fileDiskPath;
	
	/**
	 * 文件下载
	 * 
	 * @param fileName 文件名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
	public void downloadFile(String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		if (fileName != null) {
			File file = new File(fileDiskPath+fileName);
			if (file.exists()) {
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/x-msdownload");// 设置强制下载不打开
					//IE的话，通过URLEncoder对filename进行UTF8编码。
					//而其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1了
				    if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
				    	fileName = URLEncoder.encode(fileName, "UTF-8");  
				    } else {  
				    	fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");  
				    }  
					response.addHeader("Content-Disposition",
							"attachment;fileName=" + fileName);// 设置文件名
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
