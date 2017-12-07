package com.ln.tms.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * SpringUtils -Spring工具
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Lazy(false)
@Component
public final class SpringUtils implements ApplicationContextAware {
    /**
     * applicationContext
     */
    private static ApplicationContext applicationContext;

    /**
     * 不可实例化
     */
    private SpringUtils() {

    }

    /**
     * 设置 ApplicationContext
     *
     * @param applicationContext ApplicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
     * 获取 ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取实例
     *
     * @param beanName Bean的名称
     * @return Object
     */
    public static Object getBean(String beanName) {
        Assert.hasText(beanName);
        return applicationContext.getBean(beanName);

    }

    /**
     * 获取实例
     *
     * @param clazz Bean类型
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        Assert.notNull(clazz);
        return applicationContext.getBean(clazz);
    }
}