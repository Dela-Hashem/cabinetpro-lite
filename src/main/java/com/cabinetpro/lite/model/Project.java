package com.cabinetpro.lite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private Long customerId;   // FK به customers.id
    private String title;      // NOT NULL
    private String address;    // nullable
    private Instant createdAt; // از DB می‌آد
}
