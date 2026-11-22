package com.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/retail_shop_db?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";        
    private static final String PASSWORD = "Mose@42526035";            
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    private DBConnection() { }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}