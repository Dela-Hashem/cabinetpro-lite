package com.cabinetpro.lite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data @AllArgsConstructor
public class InvoiceDto {
    private Long id;
    private BigDecimal subtotal;
    private BigDecimal gst;
    private BigDecimal total;
    private String status; // DRAFT/PAID...
}
