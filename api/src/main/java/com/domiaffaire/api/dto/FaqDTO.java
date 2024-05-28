package com.domiaffaire.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaqDTO {
    private String id;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
}
