package com.ln.tms.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * FreeMarkerConfig - FreeMarker配置
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Configuration
public class FreeMarkerConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates");
        Properties properties = new Properties();
        properties.setProperty("defaultEncoding", "UTF-8");
        properties.setProperty("url_escaping_charset", "UTF-8");
        properties.setProperty("template_update_delay", "3600");
        properties.setProperty("tag_syntax", "auto_detect");
        properties.setProperty("whitespace_stripping", "true");
        properties.setProperty("classic_compatible", "true");
        properties.setProperty("number_format", "0.######");
        properties.setProperty("boolean_format", "true,false");
        properties.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
        properties.setProperty("date_format", "yyyy-MM-dd");
        properties.setProperty("time_format", "HH:mm:ss");
        properties.setProperty("object_wrapper", "freemarker.ext.beans.BeansWrapper");
        freeMarkerConfigurer.setFreemarkerSettings(properties);
        Map<String, Object> variables = new HashMap<>();
        variables.put("shiro", new ShiroTags());
        freeMarkerConfigurer.setFreemarkerVariables(variables);
        return freeMarkerConfigurer;
    }

}