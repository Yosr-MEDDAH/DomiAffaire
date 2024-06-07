package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.DomiciliationRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.exceptions.*;
import com.domiaffaire.api.repositories.DomiciliationRequestRepository;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.services.DeadlineServiceImpl;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import com.domiaffaire.api.services.ReservationServiceImpl;
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
    private final DeadlineServiceImpl deadlineService;
    private final ReservationServiceImpl reservationService;
    private final UserRepository userRepository;
    private final DomiciliationRequestRepository domiciliationRequestRepository;
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Failed to update user.\"}");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process image file.\"}");
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
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
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"User unarchived successfully\"}");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> findClientByEmail(@PathVariable String email){
        try {
            User admin = service.findUserByEmail(email);
            return ResponseEntity.ok(admin);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
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

    @GetMapping("/accountants")
    public ResponseEntity<?>  findAllComptables(){
        try {
            return ResponseEntity.ok(service.findAllAccountants());
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    //mezelt fazet l compression
    @PostMapping("/company-creation/documents")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && (file.getContentType().equals("application/pdf")
                || file.getContentType().equals("application/msword"))
                || file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            String uploadFile = service.uploadFileCompanyCreation(file);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"" + uploadFile + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only PDF or DOC files are allowed\"}");
        }
    }

    @PutMapping("/company-creation/documents/rename-file/{id}")
    public ResponseEntity<?> renameFile(@RequestBody RenameFileRequest renameFileRequest, @PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.renameFile(renameFileRequest,id));
        }catch (FileNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + ex.getMessage() + "\"}");
        }
    }

    @PutMapping("/company-creation/documents/update-file/{id}")
    public ResponseEntity<?> updateFile(@RequestParam("file") MultipartFile file, @PathVariable String id){
        try {
            if (file != null && (file.getContentType().equals("application/pdf")
                    || file.getContentType().equals("application/msword"))
                    || file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                return ResponseEntity.status(HttpStatus.OK).body(service.updateFile(file, id));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only PDF or DOC files are allowed\"}");
            }
        }catch (FileNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + ex.getMessage() + "\"}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/company-creation/documents")
    public List<FileDTO> findAllFiles(){
        return service.findAllFilesCompanyCreation();
    }

    @GetMapping("/company-creation/documents/{id}")
    public ResponseEntity<?> findFileById(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getFileById(id));
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"File not found\"}");
        }
    }

    @DeleteMapping("/company-creation/documents/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id){
        try{
            service.deleteFile(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"File deleted successfully\"}");
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/packs")
    public PackDTO addPack(@RequestBody @Valid PackRequest packRequest){
        return service.addPack(packRequest);
    }

    @PutMapping("/packs/{id}")
    public ResponseEntity<?> updatePack(@RequestBody PackRequest packRequest, @PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updatePack(packRequest, id));
        } catch (PackNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/packs/{id}")
    public ResponseEntity<?> deletePack(@PathVariable String id){
        try {
            service.deletePack(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Pack deleted successfully\"}");
        } catch (PackNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
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

    @GetMapping("/domiciliation-requests")
    public ResponseEntity<?> findAllDomiciliationRequests(){
        try {
            return ResponseEntity.ok().body(service.findAllDomiciliationRequests());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/domiciliation-requests/in-progress")
    public ResponseEntity<?> findAllDomiciliationRequestsInProgress(){
        return ResponseEntity.ok().body(service.findAllDomiciliationRequestsInProcess());
    }

    @GetMapping("/domiciliation-requests/accepted")
    public ResponseEntity<?> findAllDomiciliationRequestsAccepted(){
        return ResponseEntity.ok().body(service.findAllDomiciliationRequestsAccepted());
    }

    @GetMapping("/domiciliation-requests/rejected")
    public ResponseEntity<?> findAllDomiciliationRequestsRejected(){
        return ResponseEntity.ok().body(service.findAllDomiciliationRequestsRejected());
    }

    @GetMapping("/domiciliation-requests/{id}")
    public ResponseEntity<?> findDomiciliationRequestById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getDomiciliationRequestById(id));
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/domiciliation-requests/response-admin/accept/{id}")
    public ResponseEntity<?> acceptDomiciliationRequest(@RequestParam("draftContract") MultipartFile draftContract,
                                                        @PathVariable String id,
                                                        final HttpServletRequest request){
        try {
            if (draftContract != null && (draftContract.getContentType().equals("application/pdf")
                    || draftContract.getContentType().equals("application/msword"))
                    || draftContract.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                String message = service.acceptDomiciliationRequestAdmin(id,draftContract);
                DomiciliationRequest domiciliationRequest = domiciliationRequestRepository.findById(id).get();
                String idUser = domiciliationRequest.getClient().getId();
                User user = userRepository.findById(idUser).get();
                publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
                System.out.println(user.getEmail());
                try {
                    acceptDomiciliationEmail();
                } catch (MessagingException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error in sending mail\"}");
                }
                return ResponseEntity.ok().body("{\"message\":\""+message+ "\"}");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only PDF or DOC files are allowed\"}");
            }
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process file.\"}");

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process file.\"}");
        }
    }


    @PostMapping("/domiciliation-requests/add-patente/{id}")
    public ResponseEntity<?> addPatente(@RequestParam("patente") MultipartFile patente,
                                                        @PathVariable String id){
        try {
            if (patente != null && (patente.getContentType().equals("application/pdf")
                    || patente.getContentType().equals("application/msword"))
                    || patente.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                String message = service.addPatenteFileToDomiciliation(id,patente);
                return ResponseEntity.ok().body("{\"message\":\""+message+ "\"}");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only PDF or DOC files are allowed\"}");
            }
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process file.\"}");

        }
    }

    @PostMapping("/domiciliation-requests/add-contract/{id}")
    public ResponseEntity<?> addContract(@RequestParam("contract") MultipartFile contract,
                                        @PathVariable String id){
        try {
            if (contract != null && (contract.getContentType().equals("application/pdf")
                    || contract.getContentType().equals("application/msword"))
                    || contract.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                String message = service.addContractFileToDomiciliation(id,contract);
                return ResponseEntity.ok().body("{\"message\":\""+message+ "\"}");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only PDF or DOC files are allowed\"}");
            }
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process file.\"}");

        }
    }

    @PostMapping("/domiciliation-requests/add-existence-declaration/{id}")
    public ResponseEntity<?> addExistenceDeclaration(@RequestParam("ed") MultipartFile ed,
                                         @PathVariable String id){
        try {
            if (ed != null && (ed.getContentType().equals("application/pdf")
                    || ed.getContentType().equals("application/msword"))
                    || ed.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                String message = service.addExistenceDeclaration(id,ed);
                return ResponseEntity.ok().body("{\"message\":\""+message+ "\"}");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error: Only PDF or DOC files are allowed\"}");
            }
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to process file.\"}");

        }
    }



    @GetMapping("/response-admin/{id}")
    public ResponseEntity<?> getResponseAdminById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getResponseDomiAdminById(id));
        } catch (ResponseAdminNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/domiciliation-requests/response-admin/reject/{id}")
    public ResponseEntity<?> rejectDomiciliationRequest(@PathVariable String id,
                                                        final HttpServletRequest request){
        try{
                String message = service.rejectDomiciliationRequestAdmin(id);
                DomiciliationRequest domiciliationRequest = domiciliationRequestRepository.findById(id).get();
                String idUser = domiciliationRequest.getClient().getId();
                User user = userRepository.findById(idUser).get();
                publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            try {
                rejectDomiciliationEmail();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body("{\"message\":\""+message+ "\"}");
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private void acceptDomiciliationEmail() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendAcceptationDomiciliationRequest();
    }

    private void rejectDomiciliationEmail() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendRejectionDomiciliationRequest();
    }

    @PostMapping("/blog-categories")
    public BlogCategoryDTO addBlogCategory(@RequestBody @Valid BlogCategoryRequest blogCategoryRequest){
        return service.addBlogCategory(blogCategoryRequest);
    }

    @GetMapping("/blog-categories/{id}")
    public ResponseEntity<?> findBlogCategoryById(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getBlogCategoryById(id));
        } catch (BlogCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blog-categories")
    public ResponseEntity<?> findAllBlogCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogCategories());
    }

    @PutMapping("/blog-categories/{id}")
    public ResponseEntity<?> updateBlogCategory(@RequestBody BlogCategoryRequest blogCategoryRequest, @PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateBlogCategory(blogCategoryRequest, id));
        } catch (BlogCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/blog-categories/{id}")
    public ResponseEntity<?> deleteBlogCategorie(@PathVariable String id){
        try {
            service.deleteBlogCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Blog category deleted successfully\"}");
        } catch (BlogCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
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

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable String id){
        try {
            service.deleteBlog(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Blog deleted successfully\"}");
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/faqs")
    public FaqDTO addFaq(@RequestBody @Valid FaqRequest faqRequest){
        return service.addFaq(faqRequest);
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

    @PutMapping("/faqs/{id}")
    public ResponseEntity<?> updateFaq(@RequestBody FaqRequest faqRequest, @PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateFaq(faqRequest, id));
        } catch (FaqNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<?> deleteFaq(@PathVariable String id){
        try {
            service.deleteFaq(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Faq deleted successfully\"}");
        } catch (FaqNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/blogs-admin")
    public ResponseEntity<?> findAllAdminAuthenticatedBlogs(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogsByAuthenticatedAdmin());
        } catch (UserNotFoundException e) {
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

    @GetMapping("/domiciliation-request/client-response/{id}")
    public ResponseEntity<?> findClientResponse(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(service.getDomiciliationRequestById(id));
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/domiciliation-request/deadline/change-price/{id}")
    public ResponseEntity<?> updateDomiciliationPrice(@PathVariable String id, @RequestBody @Valid UpdateNetPayableRequest updateNetPayableRequest){
        try {
            return ResponseEntity.ok().body("{\"message\":\"" + service.updateNetPayable(id,updateNetPayableRequest) + "\"}");
        } catch (DomiciliationRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/rooms")
    public RoomDTO addRoom(@RequestBody RoomRequest roomRequest){
        RoomDTO roomDTO = reservationService.addRoom(roomRequest);
        reservationService.exportRoomsToCSV("C:/PFE/DomiAffaire/room-recommendation-system/data/rooms.csv");
        return roomDTO;
    }

    @PutMapping("/rooms/{id}")
    public RoomDTO updateRoom(@PathVariable String id,@RequestBody RoomRequest roomRequest){
        RoomDTO roomDTO = null;
        try {
            roomDTO = reservationService.updateRoom(id,roomRequest);
            reservationService.exportRoomsToCSV("C:/PFE/DomiAffaire/room-recommendation-system/data/rooms.csv");
            return roomDTO;
        } catch (RoomNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/rooms")
    public List<RoomDTO> getAllRooms(){
        return reservationService.getAllRooms();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(reservationService.getRoomById(id));
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable String id){
        try {
            reservationService.deleteRoom(id);
            return ResponseEntity.ok().body("{\"message\":\"Room deleted successfully\"}");
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/deadlines-filter")
    public ResponseEntity<?> findAllDeadlinesBetweenTwoDates(@RequestBody DeadlineFilterRequest request){
        return ResponseEntity.status(HttpStatus.OK).body((deadlineService.getDeadlinesBetweenTwoDates(request)));
    }

    @GetMapping("/deadlines")
    public ResponseEntity<?> findAllDeadlinesBetweenTwoWeeks(){
        return ResponseEntity.status(HttpStatus.OK).body((deadlineService.getDeadlinesBetweenTwoDatesFixed()));
    }

    @GetMapping("/client-deadlines/{id}")
    public ResponseEntity<?> findAllDeadlinesByClient(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body((deadlineService.getDeadlinesOfClientById(id)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/domiciliations-documents")
    public List<DomiciliationRequestDTO> findAllDomiciliationDocuments(){
        return service.findAllDomiciliationDocuments();
    }

    @GetMapping("/reservation/accept/{id}")
    public ResponseEntity<?> acceptReservation(@PathVariable String id){
        try {
            String message = reservationService.acceptReservation(id);
            reservationService.exportReservationsToCSV("C:/PFE/DomiAffaire/room-recommendation-system/data/reservations.csv");
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"" + message + "\"}");
        } catch (ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/reservation/reject/{id}")
    public ResponseEntity<?> rejectReservation(@PathVariable String id){
        try {
            String message = reservationService.rejectReservation(id);
            reservationService.exportReservationsToCSV("C:/PFE/DomiAffaire/room-recommendation-system/data/reservations.csv");
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"" + message + "\"}");
        } catch (ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/reservations")
    public List<ReservationDTO> findAll(){
        return reservationService.getAllReservations();
    }
    @GetMapping("/reservations/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(reservationService.getReservationById(id));
        } catch (ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
