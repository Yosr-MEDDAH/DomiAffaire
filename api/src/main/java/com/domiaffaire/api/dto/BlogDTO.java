package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.BlogCategory;
import com.domiaffaire.api.entities.File;
import com.domiaffaire.api.entities.User;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BlogDTO {
    private String id;
    private LocalDateTime createdAt ;
    private String title;
    private String content;
    private File image;
    private User createdBy;
    private BlogCategory category;
    private boolean isArchived;
}
