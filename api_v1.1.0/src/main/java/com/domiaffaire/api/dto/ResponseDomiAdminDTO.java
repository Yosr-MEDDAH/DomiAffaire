package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.File;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
public class ResponseDomiAdminDTO {
    private String id;
    private LocalDateTime responseDate;
    private File draftContract;
}
