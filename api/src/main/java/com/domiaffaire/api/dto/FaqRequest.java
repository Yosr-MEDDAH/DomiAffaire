package com.domiaffaire.api.dto;

import com.domiaffaire.api.validators.NoBadWords;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FaqRequest {
    @NotBlank(message = "Question shouldn't be blank")
    @NotNull(message = "Question shouldn't be null")
    @NoBadWords(message="You have typed bad words")
    private String question;
    @NotBlank(message = "Answer shouldn't be blank")
    @NotNull(message = "Answer shouldn't be null")
    @NoBadWords(message="You have typed bad words")
    private String answer;
}
