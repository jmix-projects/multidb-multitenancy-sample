# Jmix Multi-database Multitenancy Sample

This example demonstrates possible implementation of multitenancy application storing tenants data in separate databases (database per tenant).

## Overview

The application has two data stores:
1. The main data store provides access to tenant databases.
2. The additional datastore called `shared` contains data shared between all tenants. This includes the list of users and tenants.

The main data store uses [RoutingDatasource](src/main/java/com/company/multidbmt/multitenancy/RoutingDatasource.java) which creates connections to databases provided by [DataSourceRepository](src/main/java/com/company/multidbmt/multitenancy/DataSourceRepository.java). The `DataSourceRepository` class determines which database to use based on the tenant name stored in the current user session. If the current session has no information about a tenant, the default database defined for the data store is used (`multidbmt_main`). 

The [user entity](src/main/java/com/company/multidbmt/entity/User.java) has a direct link to a single tenant. This reference is used to save the tenant name in the current session upon login, see `setCurrentTenantInSession()` method of [LoginView](src/main/java/com/company/multidbmt/view/login/LoginView.java).  

The [TenantDatabaseManager](src/main/java/com/company/multidbmt/multitenancy/TenantDatabaseManager.java) class provides methods to create and drop tenant databases and run Liquibase changelogs for them. It runs Liquibase for all registered tenant databases on the application start.

The [010-init-user.xml](src/main/resources/com/company/multidbmt/liquibase/shared-changelog/010-init-user.xml) changelog for the `shared` data store creates the table for the `User` entity and inserts a record for the `admin` user.

The [010-init-role-assignment.xml](src/main/resources/com/company/multidbmt/liquibase/changelog/010-init-role-assignment.xml) changelog for the main data store (tenant-specific databases) inserts a record for assigning the `system-full-access` role to the `admin` user.

Tenant databases are created on a PostgreSQL server.

## Usage

- Make sure you have a local PostgreSQL server installed.
- Open the project in IntelliJ with the Jmix plugin installed.
- Execute the **Recreate** action for **Main Data Store** and **shared** data store to create the default main database (`multidbmt_main`) and the shared database (`multidbmt_shared`). The application configuration assumes that there is `root / root` user with admin privileges in the local PostgreSQL. Either create this user or change the application properties to use another user.
- Run the application and login as `admin / admin`.
- Open **Tenants** view and create a few tenants. Set different database connection parameters for each tenant.
- In the tenants list, click **Create database** for each tenant. The application will create a corresponding database and run the main data store Liquibase changelogs to create the schema.
- Open **Users** and create a few users. Set different tenants for them and assign `TenantUserRole`, `UI: minimal access` and `UI: edit filters` roles to them.
- Log in as different tenant users and check that data entered in the **Customers** view is stored in the tenant's database.   
- When a non-tenant user such as `admin` works with the `Customer` entity, data is stored in the default tenant database (`multidbmt_main`).