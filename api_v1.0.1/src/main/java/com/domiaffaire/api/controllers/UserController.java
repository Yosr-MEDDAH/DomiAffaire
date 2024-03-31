package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.ClientDTO;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.Client;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.mappers.Mapper;
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
    private final Mapper mapper;

    @GetMapping("/helloUser")
    public String helloUser(){
        return "Hello from user endpoint";
    }



    @GetMapping("/{email}")
    public User findClientByEmail(@PathVariable String email) throws UserNotFoundException {
            User user = service.findUserByEmail(email);
            return user;
    }
}
