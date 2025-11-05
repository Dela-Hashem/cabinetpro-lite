package com.cabinetpro.lite.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ErrorResponseDto {
    private Instant timestamp;
    private int status;            // HTTP status code
    private String error;          // e.g. "Bad Request"
    private String message;        // high-level message
    private String path;           // request path
    private List<String> details;  // validation details, optional
}
