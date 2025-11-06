package com.cabinetpro.lite.dto;

public record DbPoolPingResponse(
        String status,
        long latencyMs,
        String databaseProduct,
        String driver
) {
}
