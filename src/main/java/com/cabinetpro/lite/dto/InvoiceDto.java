package com.cabinetpro.lite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class InvoiceDto {
    private Long id;
    private double subtotal;
    private double gst;
    private double total;
    private String status; // DRAFT/PAID...
}
