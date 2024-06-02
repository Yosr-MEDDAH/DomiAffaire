package com.domiaffaire.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogCategoryDTO {
    private String id;
    private String name;
    private LocalDateTime createdAt ;
}
