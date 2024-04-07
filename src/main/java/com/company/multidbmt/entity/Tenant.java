package com.company.multidbmt.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.NumberFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "TENANT")
@Entity
public class Tenant {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "DB_HOST", nullable = false)
    @NotNull
    private String dbHost;

    @NumberFormat(pattern = "#")
    @Column(name = "DB_PORT", nullable = false)
    @NotNull
    private Integer dbPort;

    @Column(name = "DB_NAME", nullable = false)
    @NotNull
    private String dbName;

    @Column(name = "DB_CREATED")
    private Boolean dbCreated;

    @Column(name = "DB_USER")
    private String dbUser;

    @Column(name = "DB_PASSWORD")
    private String dbPassword;

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public Boolean getDbCreated() {
        return dbCreated;
    }

    public void setDbCreated(Boolean dbCreated) {
        this.dbCreated = dbCreated;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullDatabaseName() {
        return String.format("%s:%s/%s", dbHost, dbPort, dbName);
    }

    public String getDatabaseUrl() {
        return "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
    }
}