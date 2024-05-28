package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.ConsultationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "consultation_requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class ConsultationRequest {
    @Id
    private String id;
    private String details;
    private double budget;
    private String draftStatus;
    private String subject;
    private LocalDateTime sentAt;
    private LocalDateTime proposedDate;
    private LocalDateTime finalConsultationDate;
    private ConsultationStatus status = ConsultationStatus.IN_PROGRESS;
    @DBRef
    private User sentBy;
    @DBRef
    private User sentTo;
}
