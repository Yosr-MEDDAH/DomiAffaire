package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.ChangePasswordRequest;
import com.domiaffaire.api.dto.UpdateProfileRequest;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.*;
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
@RequestMapping("/api/users")
public class UserController {
    private final DomiAffaireServiceImpl service;

    @GetMapping("/all-chats/{id}")
    public ResponseEntity<?> findChatById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getChatById(id));
        } catch (ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/all-chats")
    public ResponseEntity<?> findAllChatsForAuthenticatedClient(){
        try {
            System.out.println(service.getAllChatsByAuthenticatedUserClient().size());
            return ResponseEntity.ok().body(service.getAllChatsByAuthenticatedUserClient());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/chat/messages/{id}")
    public ResponseEntity<?> findChatMessages(@PathVariable String id){
        return ResponseEntity.ok().body(service.getAllMessagesByChat(id));
    }

    @GetMapping("/blogs")
    public ResponseEntity<?> findAllBlogs(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogsNotArchived());
    }

    @GetMapping("/blogs/save/{id}")
    public ResponseEntity<?> saveBlog(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveBlogForClient(id));
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (BlogAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blogs/unsave/{id}")
    public ResponseEntity<?> unsaveBlog(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.unsaveBlogForClient(id));
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (BlogDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/saved-blogs")
    public ResponseEntity<?> findAllSavedBlogsByClient(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllSavedBlogsByClient());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/blogs/{id}")
    public ResponseEntity<?> findBlogById(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getBlogById(id));
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blogs/category/{id}")
    public ResponseEntity<?> findAllBlogsByCategory(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogsByCategory(id));
        } catch (BlogCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email){
        try {
            service.deleteAccount(email);
            return ResponseEntity.ok().body("{\"message\": \"User has been deleted successfully\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

}
