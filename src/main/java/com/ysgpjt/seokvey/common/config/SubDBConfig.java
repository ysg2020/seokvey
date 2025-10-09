package com.ysgpjt.seokvey.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Set;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.ysgpjt.seokvey.log",
        "com.ysgpjt.seokvey.consumer"},
        entityManagerFactoryRef = "subEntityManagerFactory",
        transactionManagerRef = "subTransactionManager"
)

public class SubDBConfig {

    // JPA 설정
    @Bean(name = "subEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean subEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("subDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ysgpjt.seokvey.log.entity"
                            ,"com.ysgpjt.seokvey.consumer.entity")
                .persistenceUnit("sub")
                .build();
    }

    @Bean(name = "subTransactionManager")
    public PlatformTransactionManager subTransactionManager(
            @Qualifier("subEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        // sub db 엔티티 스캔 확인
        Set<EntityType<?>> entities = Objects.requireNonNull(emf.getObject()).getMetamodel().getEntities();
        System.out.println("Managed Entities: " + entities);
        return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
    }

    // Querydsl 설정
    @Bean
    public JPAQueryFactory subQueryFactory(
            @Qualifier("subEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JPAQueryFactory(Objects.requireNonNull(emf.getObject()).createEntityManager());
    }

}
