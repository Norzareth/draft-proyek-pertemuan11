package com.example.bdsqltester;

import com.example.bdsqltester.datasources.DataSourceManager;

import java.sql.Connection;
import java.sql.SQLException;

public class testPostgresConn {
    public static void main(String[] args) {
        try (Connection conn = DataSourceManager.getUserConnection()){
            System.out.println("connected");
        } catch (SQLException e) {
            System.err.println("connectio failed");
            e.printStackTrace();

        }
    }
}
