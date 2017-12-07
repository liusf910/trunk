package com.ln.tms.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.IdleConnectionEvictor;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;

/**
 * HttpClientConfig - HttpClient配置
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Configuration
public class HttpClientConfig {

    @Value("${httpClient.maxTotal}")
    private Integer maxTotal;

    @Value("${httpClient.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${httpClient.connectTimeout}")
    private Integer connectTimeout;

    @Value("${httpClient.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${httpClient.cetSocketTimeout}")
    private Integer cetSocketTimeout;

    /**
     * 连接管理器
     */
    @Bean(destroyMethod = "close")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return poolingHttpClientConnectionManager;
    }

    /**
     * 构建器
     */
    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager());
        return httpClientBuilder;
    }

    /**
     * HttpClient
     */
    @Bean(name = "closeableHttpClient")
    @Scope(value = "prototype")
    public CloseableHttpClient closeableHttpClient() {
        return httpClientBuilder().build();
    }

    /**
     * RequestConfig构建器
     */
    @Bean
    public Builder builder() {
        Builder custom = RequestConfig.custom();
        custom.setConnectTimeout(connectTimeout);
        custom.setConnectionRequestTimeout(connectionRequestTimeout);
        custom.setSocketTimeout(cetSocketTimeout);
        return custom;
    }

    /**
     * 配置
     */
    @Bean(name = "requestConfig")
    public RequestConfig requestConfig() {
        return builder().build();

    }

    /**
     * 线程定期清理无效连接
     */
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public IdleConnectionEvictor IdleConnectionEvictor() {
        return new IdleConnectionEvictor(poolingHttpClientConnectionManager(), 5L, TimeUnit.SECONDS);
    }

}