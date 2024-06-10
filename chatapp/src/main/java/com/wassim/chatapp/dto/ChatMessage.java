package com.wassim.chatapp.dto;

import com.wassim.chatapp.validators.NoBadWords;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatMessage {
    @NoBadWords(message = "No bad words !!")
    String content;
    String user;
    String messageId;
    List<Integer> sentAt;
}
