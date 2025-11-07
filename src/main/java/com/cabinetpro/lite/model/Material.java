// src/main/java/com/cabinetpro/domain/Material.java
package com.cabinetpro.lite.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {
    private Long id;
    private Long projectId;
    private String name;
    private BigDecimal qty;
    private BigDecimal unitPrice;

}
