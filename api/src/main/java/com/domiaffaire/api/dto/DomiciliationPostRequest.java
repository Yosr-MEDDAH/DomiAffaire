package com.domiaffaire.api.dto;

import com.domiaffaire.api.validators.NoBadWords;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DomiciliationPostRequest {
    @Pattern(regexp = "^[1-2-3-4-5-6-7-8|9]\\d{7}$", message = "Invalid CIN")
    private String cin;
    @NotNull(message="denomination shouldn't be null")
    @NotBlank(message="denomination shouldn't be blank")
    @NoBadWords(message = "You have typed bad words")
    private String denomination;
    private String legalForm;
    private String companyStatus;
    private String draftStatus;
    private Double shareCapital;
    @NotNull(message="management shouldn't be null")
    @NotBlank(message="management shouldn't be blank")
    @NoBadWords(message = "You have typed bad words")
    private String management;
    private String oldDraftStatus;
    private String oldLegalForm;
    private Double oldShareCapital;
    private String oldManagement;
    private String pack;
    private String paymentMode;
}
