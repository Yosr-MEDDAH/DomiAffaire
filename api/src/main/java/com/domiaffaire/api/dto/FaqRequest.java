package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FaqRequest {
    @NotBlank(message = "Question shouldn't be blank")
    @NotNull(message = "Question shouldn't be null")
    private String question;
    @NotBlank(message = "Answer shouldn't be blank")
    @NotNull(message = "Answer shouldn't be null")
    private String answer;
}
