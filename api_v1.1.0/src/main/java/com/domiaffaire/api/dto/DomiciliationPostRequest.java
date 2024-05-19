package com.domiaffaire.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DomiciliationPostRequest {
    @Pattern(regexp = "^[1-2-3-4-5-6-7-8|9]\\d{7}$", message = "Invalid CIN")
    private String cin;
    private String denomination;
    private String legalForm;
    private String companyStatus;
    private String draftStatus;
    private Double shareCapital;
    private String management;
    private String oldDraftStatus;
    private String oldLegalForm;
    private Double oldShareCapital;
    private String oldManagement;
    private String pack;
    private String paymentMode;
}
