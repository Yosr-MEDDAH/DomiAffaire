package com.domiaffaire.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "blog_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class BlogCategory {
    @Id
    private String id;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    @JsonIgnore
    @DBRef
    List<Blog> blogs = new ArrayList<>();
}
