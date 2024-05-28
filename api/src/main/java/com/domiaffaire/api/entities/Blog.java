package com.domiaffaire.api.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "blogs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Blog {
    @Id
    private String id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String title;
    private String content;
    @DBRef
    private File image;
    @DBRef
    private User createdBy;
    @DBRef
    private BlogCategory category;
}
