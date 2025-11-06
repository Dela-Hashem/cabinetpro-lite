package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dto.DbPoolPingResponse;
import com.cabinetpro.lite.service.DatabaseHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/db")
public class DbPingController {

    private final DatabaseHealthService databaseHealthService;

    public DbPingController(DatabaseHealthService databaseHealthService) {
        this.databaseHealthService = databaseHealthService;
    }

    @GetMapping("/pool-ping")
    public ResponseEntity<DbPoolPingResponse> poolPing() throws SQLException {
        return ResponseEntity.ok(databaseHealthService.poolPing());
    }
}
