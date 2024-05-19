package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.ChangePasswordRequest;
import com.domiaffaire.api.dto.UpdateProfileRequest;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.ConsultationRequestNotFoundException;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.exceptions.WrongPasswordException;
import com.domiaffaire.api.mappers.Mapper;
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
@RequestMapping("/api/accountants")
public class AccountantController {
    private final DomiAffaireServiceImpl service;

    @GetMapping("/{email}")
    public ResponseEntity<?> findClientByEmail(@PathVariable String email){
        try {
            User comptable = service.findUserByEmail(email);
            return ResponseEntity.ok(comptable);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping(value = "/update-profile/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUser(@RequestPart("image") MultipartFile file,
                                        @RequestPart("updateRequest") @Valid UpdateProfileRequest updateProfileRequest,
                                        @PathVariable String id) {

        try {
            byte[] imageBytes = file.getBytes();
            UserDTO createdUserDto = service.updateUser(updateProfileRequest, imageBytes,"ACCOUNTANT",id);
            if (createdUserDto != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Failed to update user.\"}");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process image file.\"}");
        }
    }

    @PutMapping("/consultation-requests/accept/{id}")
    public ResponseEntity<?> validateConsultatiionRequest(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"" + service.validateConsultationRequest(id) + "\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User not found\"}");
        } catch (ConsultationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Consultation request not found\"}");
        }
    }

    @PutMapping("/consultation-requests/reject/{id}")
    public ResponseEntity<?> rejectConsultatiionRequest(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"" + service.rejectConsultationRequest(id) + "\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User not found\"}");
        } catch (ConsultationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Consultation request not found\"}");
        }
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, @PathVariable String id){
        try {
            service.changePassword(id,changePasswordRequest);
            return  ResponseEntity.ok().body("{\"message\": \"Password has been changed successfully\"}");
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/consultation-requests")
    public ResponseEntity<?> getAllConultationRequests(){
        return ResponseEntity.ok().body(service.findAllConsultationRequests());
    }

    @GetMapping("/consultation-requests/accepted-or-rejected")
    public ResponseEntity<?> getAllConultationRequestsAcceptedOrRejected(){
        return ResponseEntity.ok().body(service.findAllConsultationsAcceptedOrRejected());
    }

    @GetMapping("/consultation-requests/in-progress")
    public ResponseEntity<?> getAllConultationRequestsInProgress(){
        return ResponseEntity.ok().body(service.findAllConsultationsInProgress());
    }

    @GetMapping("/consultation-requests/accepted")
    public ResponseEntity<?> getAllConultationRequestsAccepted(){
        return ResponseEntity.ok().body(service.findAllConsultationsAccepted());
    }

    @GetMapping("/consultation-requests/rejected")
    public ResponseEntity<?> getAllConultationRequestsRejected(){
        return ResponseEntity.ok().body(service.findAllConsultationsRejected());
    }


}
