package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.ChangePasswordRequest;
import com.domiaffaire.api.dto.UpdateProfileRequest;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.exceptions.WrongPasswordException;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private final DomiAffaireServiceImpl service;
    private final Mapper mapper;
    private final UserRepository userRepository;

    @GetMapping("/helloUser")
    public String helloUser(){
        return "Hello from user endpoint";
    }



    @GetMapping("/{email}")
    public ResponseEntity<?> findClientByEmail(@PathVariable String email){
        try {
            User client = service.findUserByEmail(email);
            return ResponseEntity.ok(client);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "/update-profile/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUser(@RequestPart("image") MultipartFile file,
                                            @RequestPart("updateRequest") @Valid UpdateProfileRequest updateProfileRequest,
                                        @PathVariable String id) {

        try {
            byte[] imageBytes = file.getBytes();
            UserDTO createdUserDto = service.updateUser(updateProfileRequest, imageBytes,"CLIENT",id);
            if (createdUserDto != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image file.");
        }
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, @PathVariable String id){
        try {
            service.changePassword(id,changePasswordRequest);
            return  ResponseEntity.ok().body("{\"message\": \"Password has been changed successfully\"}");
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
