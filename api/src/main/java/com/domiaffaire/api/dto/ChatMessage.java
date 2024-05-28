package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String message;
    private User user;
}
