package com.cabinetpro.lite.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CreateWithProjectRequestDto {
    @Valid private CustomerCreateRequestDto customer;
    @Valid private ProjectCreateForCustomerDto project;
}
