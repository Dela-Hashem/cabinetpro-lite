package com.cabinetpro.lite.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateRequestDto {

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotBlank(message = "title is required")
    @Size(max = 140, message = "title must be <= 140 chars")
    private String title;

    @Size(max = 2000, message = "address too long")
    private String address;
}
