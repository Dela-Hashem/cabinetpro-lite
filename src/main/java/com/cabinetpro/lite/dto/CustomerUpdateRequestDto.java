package com.cabinetpro.lite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerUpdateRequestDto {

    @NotBlank(message = "fullName is required")
    @Size(max = 120, message = "fullName must be <= 120 chars")
    private String fullName;

    @Size(max = 40, message = "phone must be <= 40 chars")
    private String phone;

    @Email(message = "email is invalid")
    @Size(max = 120, message = "email must be <= 120 chars")
    private String email;
}
