package com.wassim.chatapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Message {
    @Id
    private String id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String content;
    @DBRef
    private  User sender;
    @DBRef
    private Chat chat;
}
