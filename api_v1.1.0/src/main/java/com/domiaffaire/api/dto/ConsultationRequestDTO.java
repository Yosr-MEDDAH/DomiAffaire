package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.ConsultationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultationRequestDTO {
    private String id;
    private String details;
    private double budget;
    private String draftStatus;
    private String subject;
    private LocalDateTime sentAt;
    private LocalDateTime proposedDate;
    private LocalDateTime finalConsultationDate;
    private ConsultationStatus status ;
    private User sentBy;
    private User sentTo;
}
