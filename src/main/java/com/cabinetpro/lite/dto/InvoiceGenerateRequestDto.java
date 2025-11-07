package com.cabinetpro.lite.dto;

import lombok.Data;

@Data
public class InvoiceGenerateRequestDto {
    // اختیاری؛ اگر null باشد، 0.10
    private Double gstRate;
}
