package com.company.multidbmt;

import com.company.multidbmt.multitenancy.DataSourceRepository;
import com.company.multidbmt.multitenancy.RoutingDatasource;
import com.google.common.base.Strings;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Push
@Theme(value = "multidbmt")
@PWA(name = "Multidbmt", shortName = "Multidbmt")
@SpringBootApplication
public class MultidbmtApplication implements AppShellConfigurator {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(MultidbmtApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("main.datasource.hikari")
    DataSourceRepository datasourceRepository(DataSourceProperties dataSourceProperties) {
        DataSource defaultDatasource = dataSourceProperties.initializeDataSourceBuilder().build();
        return new DataSourceRepository(defaultDatasource);
    }

    @Bean
    @Primary
    DataSource dataSource(DataSourceRepository datasourceRepository) {
        return new RoutingDatasource(datasourceRepository);
    }

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(MultidbmtApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }
}
