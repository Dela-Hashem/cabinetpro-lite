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
    private String status;         // DRAFT/ISSUED/PAID/VOID
    private Instant issuedAt;      // nullable تا قبل از ISSUED
    private String invoiceNumber;  // nullable تا قبل از ISSUED

}