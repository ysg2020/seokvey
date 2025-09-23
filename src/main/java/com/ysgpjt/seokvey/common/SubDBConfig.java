package com.ysgpjt.seokvey.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ysgpjt.seokvey.log",
        entityManagerFactoryRef = "subEntityManagerFactory",
        transactionManagerRef = "subTransactionManager"
)

public class SubDBConfig {

    @Bean(name = "subEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean subEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("subDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ysgpjt.seokvey.log.entity")
                .persistenceUnit("sub")
                .build();
    }

    @Bean(name = "subTransactionManager")
    public PlatformTransactionManager subTransactionManager(
            @Qualifier("subEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
    }
}
