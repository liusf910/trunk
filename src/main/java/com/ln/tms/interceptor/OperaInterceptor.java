package com.ln.tms.interceptor;

import com.ln.tms.pojo.Log;
import com.ln.tms.pojo.Permission;
import com.ln.tms.service.LogService;
import com.ln.tms.service.PermissionService;
import com.ln.tms.shiro.Principal;
import com.ln.tms.util.TmsUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * OperaInterceptor
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class OperaInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperaInterceptor.class);

    @Autowired
    private LogService logService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        /**
         * 对来自后台的请求统一进行日志处理
         */
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = getValue(request);
        LOGGER.info(String.format("请求参数, url: %s, method: %s, uri: %s, params: %s", url, method, uri, queryString));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = getValue(request);
        Principal currentUser = (Principal) SecurityUtils.getSubject().getPrincipal();

        try {
            Log log = new Log();
            log.setUserId(currentUser.getId());
            log.setIp(TmsUtils.getRemoteHost(request));
            log.setParameter(queryString);
            log.setContent(String.format("url: %s, method: %s, uri: %s", url, method, uri));
            if (logService == null) {
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                logService = (LogService) factory.getBean("logService");
                permissionService = (PermissionService) factory.getBean("permissionService");
            }
            Permission permission = permissionService.queryOne(new Permission(uri));
            if (uri.equals("/monito/exportExcel")) {
                log.setOperation("监测管理圆通文件导出");
            } else {
                log.setOperation(permission != null ? permission.getName() : "无此路径的操作");
            }
            logService.saveSelective(log);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("添加日志记录出现错误!异常={}", e.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }

    private static String getValue(HttpServletRequest request) {
        String value = "";
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            if (!"_dc".equals(paraName) && !"node".equals(paraName)) {
                String[] arr = request.getParameterValues(paraName);
                if (arr != null && arr.length > 1) {
                    value += paraName + "=" + ConvertObjectArrToStr(arr) + ";";
                } else {
                    value += paraName + "=" + request.getParameter(paraName) + ";";
                }
            }
        }
        return value;
    }

    private static String ConvertObjectArrToStr(Object[] arr) {
        String result = "";
        if (arr != null && arr.length > 0) {
            for (Object anArr : arr) {
                if (!"".equals(String.valueOf(anArr))) {
                    result += String.valueOf(anArr) + ",";
                }
            }
            if (!"".equals(result)) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }
}
