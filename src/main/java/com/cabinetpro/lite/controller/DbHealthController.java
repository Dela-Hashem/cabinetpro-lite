package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.config.DatabaseConnectionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
public class DbHealthController {

    private final DatabaseConnectionManager cm;

    public DbHealthController(DatabaseConnectionManager cm) {
        this.cm = cm;
    }

    @GetMapping("/api/db/ping")
    public ResponseEntity<String> ping() {
        long t0 = System.currentTimeMillis();
        try (Connection c = cm.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1")) {

            rs.next(); // باید true شود
            DatabaseMetaData md = c.getMetaData();
            long ms = System.currentTimeMillis() - t0;

            String info = "ok:" + rs.getInt(1) +
                    " | db=" + md.getDatabaseProductName() + " " + md.getDatabaseProductVersion() +
                    " | driver=" + md.getDriverName() +
                    " | latencyMs=" + ms;

            return ResponseEntity.ok(info);

        } catch (Exception ex) {
            // فعلاً ساده: پیام خام برای دیباگ. بعداً هندلر تمیز می‌نویسیم.
            return ResponseEntity.internalServerError().body("db error: " + ex.getMessage());
        }
    }
}
