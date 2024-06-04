package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.CompanyStatus;
import com.domiaffaire.api.enums.DomiciliationRequestStatus;
import com.domiaffaire.api.enums.LegalForm;
import com.domiaffaire.api.enums.PaymentMode;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "domiciliation_requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class DomiciliationRequest {
    @Id
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
    private LocalDateTime createdAt = LocalDateTime.now();
    @DBRef
    private User client;
    @DBRef
    private Pack pack;
    private PaymentMode paymentMode;
    @DBRef
    private Deadline deadline;
    private DomiciliationRequestStatus status = DomiciliationRequestStatus.IN_PROGRESS;
    @DBRef
    private ResponseDomiAdmin responseDomiAdmin;
    @DBRef
    private ResponseClient clientConfirmation;
    @DBRef
    private File contract;
    private boolean emailSent;
    private int protestCount=0;
    private String documentCode;
    private long domiciliationDocumentsSize;


    public void calculateDocumentsSize() {
        long totalSize = 0;

        totalSize += getFileSize(cinFile);
        totalSize += getFileSize(denominationFile);
        totalSize += getFileSize(extractRNE);
        totalSize += getFileSize(businessLicence);
        totalSize += getFileSize(existenceDeclaration);
        totalSize += getFileSize(pvChangeAddress);
        totalSize += getFileSize(oldBusinessLicence);
        totalSize += getFileSize(oldExistenceDeclaration);
        totalSize += getFileSize(contract);

        this.domiciliationDocumentsSize = totalSize;
    }

    private long getFileSize(File file) {
        return (file != null) ? file.getSize() : 0;
    }

}
