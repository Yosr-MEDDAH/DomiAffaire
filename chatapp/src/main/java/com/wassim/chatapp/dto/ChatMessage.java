package com.wassim.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    String content;
    String user;
    String messageId;
}
