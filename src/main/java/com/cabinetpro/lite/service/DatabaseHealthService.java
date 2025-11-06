package com.cabinetpro.lite.service;

import com.cabinetpro.lite.dto.DbPoolPingResponse;

import java.sql.SQLException;

public interface DatabaseHealthService {
    DbPoolPingResponse poolPing() throws SQLException;
}
