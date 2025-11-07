// MaterialUpdateRequestDto.java
package com.cabinetpro.lite.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MaterialUpdateRequestDto {
    @NotBlank
    public String name;
    @NotNull
    @DecimalMin("0.00") public BigDecimal qty;
    @NotNull @DecimalMin("0.00") public BigDecimal unitPrice;
}
