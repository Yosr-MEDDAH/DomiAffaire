package com.wassim.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatMessage {
    String content;
    String user;
    String messageId;
    List<Integer> sentAt;
    private byte[] fileContent;
}
