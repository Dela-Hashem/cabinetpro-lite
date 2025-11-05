package com.cabinetpro.lite.model;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
}
