package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.DomiciliationRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.exceptions.*;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {
    private final DomiAffaireServiceImpl service;

    @GetMapping("/{email}")
    public ResponseEntity<?> findClientByEmail(@PathVariable String email){
        try {
            User client = service.findUserByEmail(email);
            return ResponseEntity.ok(client);
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
            UserDTO createdUserDto = service.updateUser(updateProfileRequest, imageBytes,"CLIENT",id);
            if (createdUserDto != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Failed to update user.\"}");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process image file.\"}");
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

    @PostMapping("/consultion-request")
    public ResponseEntity<?> addConsultationRequest(@RequestBody @Valid ConsultationPostRequest consultationPostRequest){
        try {
            return ResponseEntity.ok().body(service.addConsultationRequest(consultationPostRequest));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/consultion-request/{id}")
    public ResponseEntity<?> cancelConsultationRequest(@PathVariable String id){
        try {
            service.cancelConsultationRequest(id);
            return ResponseEntity.ok().body("{\"message\": \"Consultation request has been canceled successfully\"}");
        } catch (ConsultationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/domiciliation-request/{id}")
    public ResponseEntity<?> cancelDomiciliationRequest(@PathVariable String id){
        try {
            service.cancelDomiciliationRequest(id);
            return ResponseEntity.ok().body("{\"message\": \"Domiciliation request has been canceled successfully\"}");
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/all-client-consultation-request")
    public ResponseEntity<?> findAllConsultationRequestsByClient(){
        try {
            return ResponseEntity.ok().body(service.findAllConsultationsByClient());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/all-client-domiciliation-request")
    public ResponseEntity<?> findAllDomiciliationRequestsByClient(){
        try {
            return ResponseEntity.ok().body(service.findAllDomiciliationRequestByClient());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/domiciliations/domiciliation-request/pp")
    public ResponseEntity<?> sendDomiciliationRequestPP(@RequestPart("domiciliation") @Valid DomiciliationPostRequest domiciliationPostRequest
            , @RequestPart("cin") MultipartFile cin
            , @RequestPart("denomination")MultipartFile denomination) throws IOException{
        try {
            return ResponseEntity.ok(service.sendDomiciliationRequestPP(domiciliationPostRequest,cin,denomination));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
        }
    }

    @PostMapping("/domiciliations/domiciliation-request/pm/in-process")
    public ResponseEntity<?> sendDomiciliationRequestPMInProcess(@RequestPart("domiciliation") @Valid DomiciliationPostRequest domiciliationPostRequest
            , @RequestPart("cin") MultipartFile cin
            , @RequestPart("denomination")MultipartFile denomination
            , @RequestPart("extractRNE")MultipartFile extractRNE) throws IOException{
        try {
            return ResponseEntity.ok(service.sendDomiciliationRequestPMInProcess(domiciliationPostRequest,cin,denomination,extractRNE));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
        }
    }

    @GetMapping("/packs/{id}")
    public ResponseEntity<?> findPackById(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getPackById(id));
        } catch (PackNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/packs")
    public ResponseEntity<?> findAllPacks(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllPacks());
    }


    @PostMapping("/domiciliations/domiciliation-request/pm/transfer")
    public ResponseEntity<?> sendDomiciliationRequestPMTransfer(@RequestPart("domiciliation") @Valid DomiciliationPostRequest domiciliationPostRequest
            ,@RequestPart("cin") MultipartFile cin
            ,@RequestPart("denomination")MultipartFile denomination
            ,@RequestPart("extractRNE")MultipartFile extractRNE
            ,@RequestPart("pvChangeAddress")MultipartFile pvChangeAddress
            ,@RequestPart("oldBusinessLicence")MultipartFile oldBusinessLicence
            ,@RequestPart("oldExistenceDeclaration")MultipartFile oldExistenceDeclaration) throws IOException{
        try {
            return ResponseEntity.ok(service.sendDomiciliationRequestPMTransfert(domiciliationPostRequest,cin,denomination,extractRNE,pvChangeAddress,oldBusinessLicence,oldExistenceDeclaration));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"message\":\""+e.getMessage()+"\"");
        }
    }

    @GetMapping("/domiciliation-requests/{id}")
    public ResponseEntity<?> findDomiciliationRequestById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getDomiciliationRequestById(id));
        } catch (DomiciliationRequestNotFoundException e) {
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



    @PostMapping("/domiciliation-requests/response-client/accept/{id}")
    public ResponseEntity<?> acceptContractTerms(@PathVariable String id) {
        try {
            String message = service.acceptContractTermsClient(id);
            return ResponseEntity.ok().body("{\"message\":\"" + message + "\"}");
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/domiciliation-requests/response-client/reject/{id}")
    public ResponseEntity<?> protestContractTerms(@PathVariable String id, @RequestBody @Valid ClientResponse clientResponse) {
        try {
            String message = service.protestContractTermsClient(id,clientResponse);
            return ResponseEntity.ok().body("{\"message\":\"" + message + "\"}");
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blogs")
    public ResponseEntity<?> findAllBlogs(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogs());
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
}
