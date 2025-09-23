package com.ysgpjt.seokvey.common;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties mainDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.sub")
    public DataSourceProperties subDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource mainDataSource() {
        return mainDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource subDataSource() {
        return subDataSourceProperties().initializeDataSourceBuilder().build();
    }


}
