package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.BlogRequest;
import com.domiaffaire.api.dto.ChangePasswordRequest;
import com.domiaffaire.api.dto.UpdateProfileRequest;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.*;
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
            return ResponseEntity.status(HttpStatus.CREATED).body(service.validateConsultationRequest(id));
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
    @GetMapping("/all-chats")
    public ResponseEntity<?> findAllChatsForAuthenticatedClient(){
        try {
            System.out.println(service.getAllChatsByAuthenticatedUserAccountant().size());
            return ResponseEntity.ok().body(service.getAllChatsByAuthenticatedUserAccountant());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/all-chats/{id}")
    public ResponseEntity<?> findChatById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getChatById(id));
        } catch (ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/chat/messages/{id}")
    public ResponseEntity<?> findChatMessages(@PathVariable String id){
        return ResponseEntity.ok().body(service.getAllMessagesByChat(id));
    }

    @PostMapping("/blogs")
    public ResponseEntity<?> addBlog(@RequestPart("blog") @Valid BlogRequest blogRequest
            ,@RequestPart("image") MultipartFile image) throws IOException{
        try {
            if (image != null && (image.getContentType().equals("image/jpeg")
                    ||  image.getContentType().equals("image/png")
                    ||  image.getContentType().equals("image/bmp"))){
                return ResponseEntity.ok(service.addBlog(blogRequest,image));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only images files are allowed\"}");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
        } catch (BlogCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
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

    @GetMapping("/blogs")
    public ResponseEntity<?> findAllBlogs(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogs());
    }

    @PutMapping("/blogs/{id}")
    public ResponseEntity<?> updateBlog(@RequestPart("blog") @Valid BlogRequest blogRequest
            ,@PathVariable String id
            ,@RequestPart("image") MultipartFile image) throws IOException{
        try {
            if (image != null && (image.getContentType().equals("image/jpeg")
                    ||  image.getContentType().equals("image/png")
                    ||  image.getContentType().equals("image/bmp"))){
                return ResponseEntity.ok(service.updateBlog(blogRequest,id,image));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only images files are allowed\"}");
            }
        } catch (BlogCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
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

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable String id){
        try {
            service.deleteBlog(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Blog deleted successfully\"}");
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/faqs/{id}")
    public ResponseEntity<?> findFaqById(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getFaqById(id));
        } catch (FaqNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/faqs")
    public ResponseEntity<?> findAllFaqs(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllFaqs());
    }

    @GetMapping("/blogs-accountant")
    public ResponseEntity<?> findAllAccountantAuthenticatedBlogs(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogsByAuthenticatedAccountant());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
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

    @GetMapping("/blog-categories")
    public ResponseEntity<?> findAllBlogCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogCategories());
    }

    @GetMapping("/blogs/archive/{id}")
    public ResponseEntity<?> archiverBlog(@PathVariable String id){
        try {
            String message = service.archiveBlog(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\""+message+"\"}");
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blogs/unarchive/{id}")
    public ResponseEntity<?> desarchiverBlog(@PathVariable String id){
        try {
            String message = service.desarchiveBlog(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\""+message+"\"}");
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blogs-archived")
    public ResponseEntity<?> findAllBlogsArchivedByAdmin(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogsArchivedByUser());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }


}
