package com.cabinetpro.lite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskItemCreateRequestDto {
    @NotNull
    public Long projectId;
    @NotBlank
    public String title;
    public boolean done = false;
    @Min(0) public int sortOrder = 0;
}