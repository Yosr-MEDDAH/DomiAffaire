package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogRequest {
    @NotNull(message="title shouldn't be null")
    @NotBlank(message = "title shouldn't be blank")
    private String title;
    @NotNull(message="content shouldn't be null")
    @NotBlank(message = "content shouldn't be blank")
    private String content;
    private String category;
}
