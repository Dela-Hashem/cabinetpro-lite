package com.cabinetpro.lite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TaskItemUpdateRequestDto {
    @NotBlank
    public String title;
    public boolean done;
    @Min(0) public int sortOrder = 0;
}