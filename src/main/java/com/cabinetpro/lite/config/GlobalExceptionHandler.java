package com.cabinetpro.lite.config;

import com.cabinetpro.lite.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 - بدنه‌ی نامعتبر (DTO با @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        ErrorResponseDto body = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Validation failed")
                .path(req.getRequestURI())
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 400 - ولیدیشن سطح پارامتر/Query (مثلاً @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {

        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        ErrorResponseDto body = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Constraint violation")
                .path(req.getRequestURI())
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 500 - خطاهای JDBC/SQL
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseDto> handleSql(
            SQLException ex, HttpServletRequest req) {

        String state = ex.getSQLState();  // کد استاندارد Postgres/SQL
        HttpStatus status;
        String message;

        // نگاشت مهم‌ترین خطاهای پایگاه‌داده به HTTP
        switch (state) {
            case "23503" -> { // foreign_key_violation
                status = HttpStatus.BAD_REQUEST;
                message = "Invalid reference: related record not found (e.g., customerId).";
            }
            case "23505" -> { // unique_violation
                status = HttpStatus.CONFLICT;
                message = "Duplicate value violates a unique constraint.";
            }
            case "23502" -> { // not_null_violation
                status = HttpStatus.BAD_REQUEST;
                message = "Required field is null.";
            }
            case "22P02" -> { // invalid_text_representation (e.g., UUID parsing)
                status = HttpStatus.BAD_REQUEST;
                message = "Invalid value format.";
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                message = "Database error";
            }
        }

        ErrorResponseDto body = ErrorResponseDto.builder()
                .timestamp(java.time.Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .details(java.util.List.of(state + " / " + ex.getErrorCode()))
                .build();

        return ResponseEntity.status(status).body(body);
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleBadJson(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            jakarta.servlet.http.HttpServletRequest req) {

        ErrorResponseDto body = ErrorResponseDto.builder()
                .timestamp(java.time.Instant.now())
                .status(org.springframework.http.HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Malformed JSON body")
                .path(req.getRequestURI())
                .details(java.util.List.of(Optional.ofNullable(ex.getMostSpecificCause())
                        .map(Throwable::getMessage)
                        .orElse(ex.getMessage())))
                .build();
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOther(
            Exception ex, HttpServletRequest req) {

        ex.printStackTrace(); // موقت: استک‌تریس کامل در کنسول

        ErrorResponseDto body = ErrorResponseDto.builder()
                .timestamp(java.time.Instant.now())
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getClass().getName() + ": " + String.valueOf(ex.getMessage()))
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
