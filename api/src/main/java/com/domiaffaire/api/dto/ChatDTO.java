package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.Message;
import com.domiaffaire.api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private String id;
    private LocalDateTime createdAt;
    private User accountant;
    private User client;
    private List<Message> messages ;
}
