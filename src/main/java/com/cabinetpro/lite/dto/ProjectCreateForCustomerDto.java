package com.cabinetpro.lite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateForCustomerDto {
    @NotBlank(message = "title is required")
    @Size(max = 140, message = "title must be <= 140 chars")
    private String title;

    @Size(max = 2000, message = "address too long")
    private String address;
}
