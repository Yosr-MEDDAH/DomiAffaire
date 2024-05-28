package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PackRequest {
    @NotBlank(message = "Designation shouldn't be blank")
    private String designation;
    @NotNull(message = "Price shouldn't be null")
    private float price;
    @NotBlank(message = "Description shouldn't be blank")
    private String description;
}
