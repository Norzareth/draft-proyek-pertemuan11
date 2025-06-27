package com.example.bdsqltester.datasources;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceManager {
    private static HikariDataSource userData;

    static {
        userData = createDataSource("squire", "schoolstuff");

    }

    private static HikariDataSource createDataSource(String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/bd_project");
        config.setUsername(username);
        config.setPassword(password);
        /*
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

         */
        return new HikariDataSource(config);
    }

    public static Connection getUserConnection() throws SQLException {
        Connection conn = userData.getConnection();
        System.out.println("Connection established: " + conn);
        return conn;
    }

    private DataSourceManager() {
    }




}
