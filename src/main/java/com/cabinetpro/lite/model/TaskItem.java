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
public class TaskItem {
    private Long id;
    private Long projectId;
    private String title;
    private boolean done;
    private int sortOrder;

    // getters/setters
}
