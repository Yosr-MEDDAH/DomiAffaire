package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogCategoryRequest {
    @NotNull(message="name shouldn't be null")
    @NotBlank(message = "name shouldn't be blank")
    private String name;
}
