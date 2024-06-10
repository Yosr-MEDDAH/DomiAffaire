package com.domiaffaire.api.dto;

import com.domiaffaire.api.validators.NoBadWords;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultationPostRequest {
    @NotNull(message="details shouldn't be null")
    @NotBlank(message="details shouldn't be blank")
    @NoBadWords(message = "You have typed bad words")
    private String details;
    @NotNull(message="budget shouldn't be null")
    private double budget;
    @NotNull(message="draftStatus shouldn't be null")
    @NotBlank(message="draftStatus shouldn't be blank")
    private String draftStatus;
    @NotNull(message="subject shouldn't be null")
    @NotBlank(message="subject shouldn't be blank")
    private String subject;
    private LocalDateTime proposedDate;
}
