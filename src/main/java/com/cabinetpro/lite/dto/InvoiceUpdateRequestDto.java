package com.cabinetpro.lite.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
public class InvoiceUpdateRequestDto {

    @NotNull @DecimalMin("0.00") public BigDecimal subtotal;
    @NotNull @DecimalMin("0.00") public BigDecimal gst;
    @NotNull @DecimalMin("0.00") public BigDecimal total;
    @NotBlank public String status; // DRAFT/SENT/PAID

}
