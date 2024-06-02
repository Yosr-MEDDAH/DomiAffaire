package com.domiaffaire.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeadlineFilterRequest {
    private LocalDateTime date1;
    private LocalDateTime date2;
}
