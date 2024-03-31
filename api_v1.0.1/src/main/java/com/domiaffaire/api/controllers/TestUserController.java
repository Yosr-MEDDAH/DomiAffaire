package com.domiaffaire.api.controllers;

import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.services.AuthService;
import com.domiaffaire.api.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestUserController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/helloUser")
    public String helloUser(){
        return "Hello from user endpoint";
    }


}
