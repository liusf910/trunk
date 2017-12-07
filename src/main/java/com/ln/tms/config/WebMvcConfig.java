package com.ln.tms.config;

import com.ln.tms.interceptor.OperaInterceptor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WebMvcConfig - MVC配置
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${jetty.maxThreads}")
    private String maxThreads;

    @Value("${jetty.minThreads}")
    private String minThreads;

    @Value("${jetty.idleTimeout}")
    private String idleTimeout;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OperaInterceptor()).addPathPatterns("/setting/**/add**")
                .addPathPatterns("/setting/**/edit**").addPathPatterns("/setting/**/delete**")
                .addPathPatterns("/upload/**").addPathPatterns("/tooksign/**/delete**")
                .addPathPatterns("/monito/exportExcel");
    }

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            JettyServerCustomizer jettyServerCustomizer) {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addServerCustomizers(jettyServerCustomizer);
        return factory;
    }

    @Bean
    public JettyServerCustomizer jettyServerCustomizer() {
        return new JettyServerCustomizer() {
            @Override
            public void customize(Server server) {
                final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
                threadPool.setMaxThreads(Integer.parseInt(maxThreads));
                threadPool.setMinThreads(Integer.parseInt(minThreads));
                threadPool.setIdleTimeout(Integer.parseInt(idleTimeout));
            }
        };
    }
}