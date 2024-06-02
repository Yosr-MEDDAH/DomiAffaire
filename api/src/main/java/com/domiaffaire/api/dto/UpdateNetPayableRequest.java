package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNetPayableRequest {
    @NotNull
    private float price;
}
