package com.company.multidbmt;

import io.jmix.autoconfigure.data.JmixLiquibaseCreator;
import io.jmix.core.JmixModules;
import io.jmix.core.Resources;
import io.jmix.data.impl.JmixEntityManagerFactoryBean;
import io.jmix.data.impl.JmixTransactionManager;
import io.jmix.data.persistence.DbmsSpecifics;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class SharedStoreConfiguration {

    @Bean
    @ConfigurationProperties("shared.datasource")
    DataSourceProperties sharedDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "shared.datasource.hikari")
    DataSource sharedDataSource(@Qualifier("sharedDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean sharedEntityManagerFactory(
            @Qualifier("sharedDataSource") DataSource dataSource,
            JpaVendorAdapter jpaVendorAdapter,
            DbmsSpecifics dbmsSpecifics,
            JmixModules jmixModules,
            Resources resources
    ) {
        return new JmixEntityManagerFactoryBean("shared", dataSource, jpaVendorAdapter, dbmsSpecifics, jmixModules, resources);
    }

    @Bean
    JpaTransactionManager sharedTransactionManager(@Qualifier("sharedEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JmixTransactionManager("shared", entityManagerFactory);
    }

    @Bean("sharedLiquibaseProperties")
    @ConfigurationProperties(prefix = "shared.liquibase")
    public LiquibaseProperties sharedLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean("sharedLiquibase")
    public SpringLiquibase sharedLiquibase(@Qualifier("sharedDataSource") DataSource dataSource,
                                           @Qualifier("sharedLiquibaseProperties") LiquibaseProperties liquibaseProperties) {
        return JmixLiquibaseCreator.create(dataSource, liquibaseProperties);
    }
}
