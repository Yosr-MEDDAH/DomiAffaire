package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.File;
import com.domiaffaire.api.entities.Pack;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.CompanyStatus;
import com.domiaffaire.api.enums.LegalForm;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DomiciliationDTO {
    private String id;
    private String cin;
    private File cinFile;
    private String denomination;
    private File denominationFile;
    private File businessLicence;
    private File existenceDeclaration;
    private CompanyStatus companyStatus;
    private LegalForm legalForm;
    private File extractRNE;
    private String draftStatus;
    private File domiciliationContract;
    private String management;
    private Double shareCapital;
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
}
