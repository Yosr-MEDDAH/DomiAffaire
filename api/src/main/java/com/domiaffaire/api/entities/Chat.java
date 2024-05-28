package com.domiaffaire.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Chat {
    @Id
    private String id;
    private LocalDateTime createdAt = LocalDateTime.now();
    @DBRef
    private User accountant;
    @DBRef
    private User client;
    @JsonIgnore
    @DBRef
    private List<Message> messages = new ArrayList<>();
}
