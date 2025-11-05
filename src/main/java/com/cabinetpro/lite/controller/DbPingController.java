//// src/main/java/com/cabinetpro/lite/controller/DbPingController.java
//package com.cabinetpro.lite.controller;
//
//import com.cabinetpro.lite.config.DatabaseConnectionManager;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//@RestController
//public class DbPingController {
//    private final DatabaseConnectionManager cm;
//    public DbPingController(DatabaseConnectionManager cm) { this.cm = cm; }
//
//    @GetMapping("/api/db/ping")
//    public String ping() throws Exception {
//        try (Connection c = cm.getConnection();
//             PreparedStatement ps = c.prepareStatement("SELECT 1");
//             ResultSet rs = ps.executeQuery()) {
//            rs.next();
//            return "OK:" + rs.getInt(1);
//        }
//    }
//}
