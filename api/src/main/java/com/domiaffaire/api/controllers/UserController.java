package com.domiaffaire.api.controllers;

import com.domiaffaire.api.exceptions.ChatNotFoundException;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final DomiAffaireServiceImpl service;
    @GetMapping("/test")
    public String test(){
        return "Hello user";
    }

    @GetMapping("/all-chats/{id}")
    public ResponseEntity<?> findChatById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getChatById(id));
        } catch (ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
