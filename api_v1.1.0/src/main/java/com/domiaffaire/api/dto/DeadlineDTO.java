package com.domiaffaire.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeadlineDTO {
    private String id;
    private LocalDateTime dateBeginig;
}
