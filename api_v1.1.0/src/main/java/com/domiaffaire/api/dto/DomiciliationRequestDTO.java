package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.File;
import com.domiaffaire.api.entities.Pack;
import com.domiaffaire.api.entities.ResponseDomiAdmin;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.CompanyStatus;
import com.domiaffaire.api.enums.DomiciliationRequestStatus;
import com.domiaffaire.api.enums.LegalForm;
import com.domiaffaire.api.enums.PaymentMode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DomiciliationRequestDTO {
    private String id;
    private String cin;
    private File cinFile;
    private String denomination;
    private File denominationFile;
    private LegalForm legalForm;
    private CompanyStatus companyStatus;
    private File extractRNE;
    private String draftStatus;
    private Double shareCapital;
    private String management;
    private File pvChangeAddress;
    //ancien patente
    private File oldBusinessLicence;
    private File oldExistenceDeclaration;
    private String oldDraftStatus;
    private LegalForm oldLegalForm;
    private Double oldShareCapital;
    private String oldManagement;
    private LocalDateTime createdAt;
    private User client;
    private Pack pack;
    private DomiciliationRequestStatus status;
    private ResponseDomiAdmin responseDomiAdmin;
    private PaymentMode paymentMode;
}
