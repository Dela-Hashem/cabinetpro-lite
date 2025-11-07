package com.cabinetpro.lite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceGenerateRequestDto {
    // اختیاری؛ اگر null باشد، 0.10
    private BigDecimal gstRate;
}
