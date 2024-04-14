package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.exceptions.NoContentException;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final DomiAffaireServiceImpl service;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final RegistrationCompleteEventListener eventListener;

    @PutMapping(value = "/update-profile/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUser(@RequestPart("image") MultipartFile file,
                                        @RequestPart("updateRequest") @Valid UpdateAdminProfileRequest updateAdminProfileRequest,
                                        @PathVariable String id) {

        try {
            byte[] imageBytes = file.getBytes();
            UserDTO createdUserDto = service.updateAdmin(updateAdminProfileRequest, imageBytes,id);
            if (createdUserDto != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image file.");
        }
    }

    @PutMapping("/clients/archive/{id}")
    public ResponseEntity<?> archiveUser(@PathVariable String id,final HttpServletRequest request){
        try {
            service.archiveProfile(id);
            User user = userRepository.findById(id).get();
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            try {
                archiveEmailLink();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"User archived successfully\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    private void archiveEmailLink() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendDisableAccountMessage();
    }

    @PutMapping("/clients/unarchive/{id}")
    public ResponseEntity<?> unarchiveUser(@PathVariable String id){
        try {
            service.unarchiveProfile(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("User Unarchived successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> findClientByEmail(@PathVariable String email){
        try {
            User admin = service.findUserByEmail(email);
            return ResponseEntity.ok(admin);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/clients")
    public ResponseEntity<?> findAllClients(){
        try {
            List<ClientDTO> users = service.findAllClients();
            return ResponseEntity.ok(users);
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
    }

    @GetMapping("/clients/archived")
    public ResponseEntity<?> findAllClientsArchived(){
        try {
            List<ClientDTO> users = service.findAllClientsArchived();
            return ResponseEntity.ok(users);
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
    }

    @GetMapping("/comptables")
    public ResponseEntity<?>  findAllComptables(){
        try {
            return ResponseEntity.ok(service.findAllComptables());
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }
}
