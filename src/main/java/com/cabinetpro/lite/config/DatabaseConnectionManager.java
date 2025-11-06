//package com.cabinetpro.lite.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//@Component
//public class DatabaseConnectionManager {
//
//    private final String url;
//    private final String user;
//    private final String pass;
//
//    public DatabaseConnectionManager(
//            @Value("${app.db.url}") String url,
//            @Value("${app.db.username}") String user,
//            @Value("${app.db.password}") String pass) {
//        this.url = url;
//        this.user = user;
//        this.pass = pass;
//    }
//
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(url, user, pass);
//    }
//}
