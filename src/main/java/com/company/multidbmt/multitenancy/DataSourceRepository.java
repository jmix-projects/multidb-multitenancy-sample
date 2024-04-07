package com.company.multidbmt.multitenancy;

import com.company.multidbmt.entity.Tenant;
import com.company.multidbmt.entity.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jmix.core.security.CurrentAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean that initializes, stores and returns {@link DataSource}s for {@link Tenant}s associated with the current user.
 * If the tenant cannot be determined, returns the default datasource.
 * <p>
 * Instantiated in {@link com.company.multidbmt.TenantStoreConfiguration}.
 */
public class DataSourceRepository {

    private final static Logger log = LoggerFactory.getLogger(DataSourceRepository.class);

    private final Map<Tenant, DataSource> tenantToDatasourceMap = new ConcurrentHashMap<>();

    private final DataSource defaultDataSource;
    private final CurrentAuthentication currentAuthentication;

    public DataSourceRepository(DataSource defaultDataSource,
                                CurrentAuthentication currentAuthentication) {
        this.defaultDataSource = defaultDataSource;
        this.currentAuthentication = currentAuthentication;
    }

    public DataSource getDatasource() {
        if (!currentAuthentication.isSet()) {
            log.debug("Current thread is not authenticated");
            return getDefaultDataSource();
        }

        if (currentAuthentication.getUser() instanceof User user) {
            if (user.getTenant() == null) {
                log.debug("Current user does not belong to a tenant");
                return getDefaultDataSource();
            }

            return tenantToDatasourceMap.computeIfAbsent(user.getTenant(), this::createDataSource);
        } else {
            log.warn("Current user is not of User entity type");
            return getDefaultDataSource();
        }
    }

    public DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    private DataSource createDataSource(Tenant tenant) {
        log.info("Creating DataSource for {}", tenant.getFullDatabaseName());
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(tenant.getDatabaseUrl());
        hikariConfig.setUsername(tenant.getDbUser());
        hikariConfig.setPassword(tenant.getDbPassword());

        return new HikariDataSource(hikariConfig);
    }
}
