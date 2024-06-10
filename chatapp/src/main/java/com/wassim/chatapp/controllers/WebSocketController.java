package com.wassim.chatapp.controllers;

import com.wassim.chatapp.dto.ChatMessage;
import com.wassim.chatapp.services.WebSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final WebSocketService service;
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, @Header("userId") String userId,@Payload @Valid ChatMessage content){
        return service.sendMessage(roomId,userId,content);
    }
}
