package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.util.TmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.util.Map;

/**
 * BaseController - 基类
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class BaseController {
    /**
     * Logger
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 错误视图
     */
    protected static final String ERROR_VIEW = "error";

    /**
     * 数据绑定
     *
     * @param binder WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * 异常处理
     *
     * @param exception Exception
     * @return ExecuteResult
     */
    @ExceptionHandler
    public Map<String, Object> exceptionHandler(Exception exception, HttpServletRequest request) {
        ExecuteResult result = new ExecuteResult();
        if (exception instanceof ValidationException) {
            logger.error("Client IP={},ValidationException", TmsUtils.getRemoteHost(request), exception);
            return result.jsonReturn(HttpServletResponse.SC_OK, exception.getMessage());
        }
        logger.error("Client Ip={}, Handler execution resulted in exception={}", TmsUtils.getRemoteHost(request), exception);
        return result.jsonReturn(HttpServletResponse.SC_MULTIPLE_CHOICES, exception.getMessage());

    }
}
