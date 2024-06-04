package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.*;
import com.domiaffaire.api.enums.CompanyStatus;
import com.domiaffaire.api.enums.DomiciliationRequestStatus;
import com.domiaffaire.api.enums.LegalForm;
import com.domiaffaire.api.enums.PaymentMode;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
public class DomiciliationRequestDTO {
    private String id;
    private String cin;
    @DBRef
    private File cinFile;
    private String denomination;
    @DBRef
    private File denominationFile;
    private LegalForm legalForm;
    private CompanyStatus companyStatus;
    @DBRef
    private File extractRNE;
    private String draftStatus;
    private Double shareCapital;
    private String management;
    @DBRef
    private File businessLicence;
    @DBRef
    private File existenceDeclaration;
    @DBRef
    private File pvChangeAddress;
    //ancien patente
    @DBRef
    private File oldBusinessLicence;
    @DBRef
    private File oldExistenceDeclaration;
    private String oldDraftStatus;
    private LegalForm oldLegalForm;
    private Double oldShareCapital;
    private String oldManagement;
    private LocalDateTime createdAt ;
    @DBRef
    private User client;
    @DBRef
    private Pack pack;
    private PaymentMode paymentMode;
    @DBRef
    private Deadline deadline;
    private DomiciliationRequestStatus status;
    @DBRef
    private ResponseDomiAdmin responseDomiAdmin;
    @DBRef
    private ResponseClient clientConfirmation;
    @DBRef
    private File contract;
    private boolean emailSent;
    private int protestCount;
    private String documentCode;
    private long domiciliationDocumentsSize;
}
