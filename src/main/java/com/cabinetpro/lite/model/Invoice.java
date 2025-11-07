package com.cabinetpro.lite.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    private Long id;
    private Long projectId;
    private BigDecimal subtotal;
    private BigDecimal gst;
    private BigDecimal total;
    private String status; // DRAFT, SENT, PAID, ...
    private Instant issuedAt;

}