package com.ln.tms.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * DataSourceConfig - 数据源配置
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Configuration
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @ConfigurationProperties(prefix = "datasource.tms")
    @Bean(name = "tmsDataSource", initMethod = "init", destroyMethod = "close")
    @Primary
    public DataSource toolsDataSource() {
        LOGGER.info("==================== init tmsDataSource ====================");
        return new DruidDataSource();
    }

    @ConfigurationProperties(prefix = "datasource.erp")
    @Bean(name = "erpDataSource")
    public DataSource erpDataSource() {
        LOGGER.info("==================== init erpDataSource ====================");
        return new ComboPooledDataSource();
    }

}