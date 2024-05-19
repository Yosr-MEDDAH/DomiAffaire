package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.dto.ClientResponse;
import com.domiaffaire.api.entities.*;
import com.domiaffaire.api.enums.*;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.exceptions.*;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DomiAffaireServiceImpl implements DomiAffaireService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final Mapper mapper;
    private final DomiciliationRequestRepository domiciliationRequestRepository;
    private final PackRepository packRepository;
    private final ResponseDomiAdminRepository responseDomiAdminRepository;
    private final ResponseClientRepository responseClientRepository;
    private final DeadlineRepository deadlineRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final ApplicationEventPublisher publisher;

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findFirstByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user;
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public void deleteFile(String id) throws FileNotFoundException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if(fileOptional.isPresent()){
            File file = fileOptional.get();
            fileRepository.delete(file);
        }else{
            throw new FileNotFoundException("File not found");
        }
    }



    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = users.stream()
                .map(user->mapper.fromUserToUserDTO(user))
                .collect(Collectors.toList());
        return userDTOS;
    }



    @Override
    public List<ClientDTO> findAllClientsArchived() throws NoContentException {
        List<User> users = userRepository.findAllByUserRole(UserRole.CLIENT);
        List<User> clientsArchived = new ArrayList<>();
        for(User elmt:users){
            if(!elmt.isNotArchived()){
                clientsArchived.add(elmt);
            }
        }
        if (clientsArchived.isEmpty()) {
            throw new NoContentException("No clients found.");
        }
        List<ClientDTO> clientsDTOS = clientsArchived.stream()
                .map(client->mapper.fromUserToClientDTO(client))
                .collect(Collectors.toList());
        return clientsDTOS;
    }

    @Override
    public List<FileDTO> findAllFiles() {
        List<File> files = fileRepository.findAll();
        List<FileDTO> fileDTOS = files.stream()
                .map(file->mapper.fromFileToFileDTO(file))
                .collect(Collectors.toList());
        return fileDTOS;
    }

    @Override
    public List<FileDTO> findAllFilesCompanyCreation() {
        List<File> files = fileRepository.findAllByCompanyCreationIsTrue();
        List<FileDTO> fileDTOS = files.stream()
                .map(file->mapper.fromFileToFileDTO(file))
                .collect(Collectors.toList());
        return fileDTOS;
    }



    @Override
    public List<ClientDTO> findAllClients() throws NoContentException {
        List<User> users = userRepository.findAllByUserRole(UserRole.CLIENT);

        if (users.isEmpty()) {
            throw new NoContentException("No clients found.");
        }
        else{
            List<User> clients= new ArrayList<>();
            for(User elmt:users){
                if(elmt.isEnabled() && elmt.isNotArchived()){
                    clients.add(elmt);
                }
            }
            return clients.stream()
                    .map(client -> mapper.fromUserToClientDTO(client))
                    .collect(Collectors.toList());
        }
    }


    @Override
    public List<AccountantDTO> findAllAccountants() throws NoContentException {
        List<User> users = userRepository.findAllByUserRole(UserRole.ACCOUNTANT);

        if (users.isEmpty()) {
            throw new NoContentException("No clients found.");
        }else{
            return users.stream()
                    .map(user -> mapper.fromUserToAccountantDTO(user))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsByClient() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllBySentBy(authenticatedUser);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestByClient() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByClient(authenticatedUser);
        return domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationRequests() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAll();
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsInProgress() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIs(ConsultationStatus.IN_PROGRESS);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsAccepted() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIs(ConsultationStatus.ACCEPTED);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsRejected() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIs(ConsultationStatus.REJECTED);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsAcceptedOrRejected() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIsOrStatusIs(ConsultationStatus.ACCEPTED,ConsultationStatus.REJECTED);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public void cancelConsultationRequest(String id) throws ConsultationRequestNotFoundException{
        Optional<ConsultationRequest> optionalConsultationRequest = consultationRequestRepository.findById(id);
        if(optionalConsultationRequest.isPresent()){
            consultationRequestRepository.delete(optionalConsultationRequest.get());
        }else {
            throw new ConsultationRequestNotFoundException("Consultation request not found");
        }
    }

    @Override
    public void cancelDomiciliationRequest(String id) throws DomiciliationRequestNotFoundException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            domiciliationRequestRepository.delete(domiciliationRequestOptional.get());
        }else{
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String validateConsultationRequest(String id) throws UserNotFoundException, ConsultationRequestNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        ConsultationRequest consultationRequest = consultationRequestRepository.findById(id).orElseThrow(()->new ConsultationRequestNotFoundException("Consultation request not found"));
        consultationRequest.setSentTo(authenticatedUser);
        consultationRequest.setFinalConsultationDate(LocalDateTime.now());
        consultationRequest.setStatus(ConsultationStatus.ACCEPTED);
        consultationRequestRepository.save(consultationRequest);
        String clientId = consultationRequest.getSentBy().getId();
        String accountantId = authenticatedUser.getId();
        ChatDTO createdChatDto = createChat(clientId,accountantId);
        return "Consultation Request Validated";
    }

    @Override
    public String rejectConsultationRequest(String id) throws UserNotFoundException, ConsultationRequestNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        ConsultationRequest consultationRequest = consultationRequestRepository.findById(id).orElseThrow(()->new ConsultationRequestNotFoundException("Consultation request not found"));
        consultationRequest.setSentTo(authenticatedUser);
        consultationRequest.setFinalConsultationDate(LocalDateTime.now());
        consultationRequest.setStatus(ConsultationStatus.REJECTED);
        consultationRequestRepository.save(consultationRequest);
        return "Consultation Request Rejected";
    }

    @Override
    public ChatMessage sendMessage(String chatId, ChatMessage chatMessage) throws ChatNotFoundException, UserNotFoundException {
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new ChatNotFoundException("Chat not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        Message message = new Message();
        message.setContent(chatMessage.getMessage());
        message.setSender(authenticatedUser);
        message.setChat(chat);
        chat.getMessages().add(message);
        messageRepository.save(message);
        chatRepository.save(chat);
        return chatMessage;
    }

    @Override
    public ChatDTO createChat(String clientId, String accountantId) throws UserNotFoundException {
        User client = userRepository.findById(clientId).orElseThrow(()->new UserNotFoundException("Client not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User accountant = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (accountant == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        Chat chat = new Chat();
        chat.setAccountant(accountant);
        chat.setClient(client);
        chatRepository.save(chat);
        return mapper.fromChatToChatDTO(chat);
    }

    @Override
    public List<ChatDTO> getAllChatsByAuthenticatedUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<Chat> chats = chatRepository.findAllByAccountantIsOrClientIs(authenticatedUser,authenticatedUser);
        List<ChatDTO> chatDTOS = chats.stream()
                .map(chat -> mapper.fromChatToChatDTO(chat))
                .collect(Collectors.toList());
        return chatDTOS;
    }

    @Override
    public ChatDTO getChatById(String id) throws ChatNotFoundException {
        Chat chat = chatRepository.findById(id).orElseThrow(()->new ChatNotFoundException("Chat not found"));
        return mapper.fromChatToChatDTO(chat);
    }

    @Override
    public FileDTO getFileById(String id) throws FileNotFoundException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            return mapper.fromFileToFileDTO(file);
        }else{
            throw new FileNotFoundException("File not found");
        }
    }

    @Override
    public DomiciliationRequestDTO sendDomiciliationRequestPP(DomiciliationPostRequest domiciliationPostRequest,MultipartFile cin,MultipartFile denomination) throws UserNotFoundException,IOException {
        DomiciliationRequest domiciliationRequest = new DomiciliationRequest();
        domiciliationRequest.setCin(domiciliationPostRequest.getCin());
        domiciliationRequest.setDenomination(domiciliationPostRequest.getDenomination());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        domiciliationRequest.setClient(authenticatedUser);

        if(domiciliationPostRequest.getCompanyStatus().equals("In Process")){
            domiciliationRequest.setCompanyStatus(CompanyStatus.IN_PROCESS);
        }else if(domiciliationPostRequest.getCompanyStatus().equals("Transfer")){
            domiciliationRequest.setCompanyStatus(CompanyStatus.TRANSFER);
        }
        domiciliationRequest.setLegalForm(LegalForm.NATURAL_PERSON);
        File savedCin = fileRepository.save(File.builder()
                .name(cin.getOriginalFilename())
                .type(cin.getContentType())
                .fileData(cin.getBytes())
                .build());

        File savedDenomination = fileRepository.save(File.builder()
                .name(denomination.getOriginalFilename())
                .type(denomination.getContentType())
                .fileData(denomination.getBytes())
                .build());

        Pack pack = packRepository.findById(domiciliationPostRequest.getPack()).get();
        domiciliationRequest.setPack(pack);

        if(domiciliationPostRequest.getPaymentMode().equals("Quarter")){
            domiciliationRequest.setPaymentMode(PaymentMode.QUARTER);
        }else if(domiciliationPostRequest.getPaymentMode().equals("Semester")){
            domiciliationRequest.setPaymentMode(PaymentMode.SEMESTER);
        }else if(domiciliationPostRequest.getPaymentMode().equals("Annually")){
            domiciliationRequest.setPaymentMode(PaymentMode.ANNUALLY);
        }

        domiciliationRequest.setCinFile(savedCin);
        domiciliationRequest.setDenominationFile(savedDenomination);

        domiciliationRequestRepository.save(domiciliationRequest);
        return mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest);
    }

    @Override
    public DomiciliationRequestDTO sendDomiciliationRequestPMInProcess(DomiciliationPostRequest domiciliationPostRequest, MultipartFile cin, MultipartFile denomination, MultipartFile extractRNE) throws UserNotFoundException, IOException {
        DomiciliationRequest domiciliationRequest = new DomiciliationRequest();
        domiciliationRequest.setCin(domiciliationPostRequest.getCin());
        domiciliationRequest.setDenomination(domiciliationPostRequest.getDenomination());
        domiciliationRequest.setDraftStatus(domiciliationPostRequest.getDraftStatus());
        domiciliationRequest.setShareCapital(domiciliationPostRequest.getShareCapital());
        domiciliationRequest.setManagement(domiciliationPostRequest.getManagement());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        domiciliationRequest.setClient(authenticatedUser);


        domiciliationRequest.setCompanyStatus(CompanyStatus.IN_PROCESS);

        domiciliationRequest.setLegalForm(LegalForm.CORPORATION);
        File savedCin = fileRepository.save(File.builder()
                .name(cin.getOriginalFilename())
                .type(cin.getContentType())
                .fileData(cin.getBytes())
                .build());

        File savedDenomination = fileRepository.save(File.builder()
                .name(denomination.getOriginalFilename())
                .type(denomination.getContentType())
                .fileData(denomination.getBytes())
                .build());

        File savedExtractRNE = fileRepository.save(File.builder()
                .name(extractRNE.getOriginalFilename())
                .type(extractRNE.getContentType())
                .fileData(extractRNE.getBytes())
                .build());

        Pack pack = packRepository.findById(domiciliationPostRequest.getPack()).get();
        domiciliationRequest.setPack(pack);

        if(domiciliationPostRequest.getPaymentMode().equals("Quarter")){
            domiciliationRequest.setPaymentMode(PaymentMode.QUARTER);
        }else if(domiciliationPostRequest.getPaymentMode().equals("Semester")){
            domiciliationRequest.setPaymentMode(PaymentMode.SEMESTER);
        }else if(domiciliationPostRequest.getPaymentMode().equals("Annually")){
            domiciliationRequest.setPaymentMode(PaymentMode.ANNUALLY);
        }

        domiciliationRequest.setCinFile(savedCin);
        domiciliationRequest.setDenominationFile(savedDenomination);
        domiciliationRequest.setExtractRNE(savedExtractRNE);

        domiciliationRequestRepository.save(domiciliationRequest);
        return mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest);
    }

    @Override
    public DomiciliationRequestDTO sendDomiciliationRequestPMTransfert(DomiciliationPostRequest domiciliationPostRequest,
                                                                       MultipartFile cin,
                                                                       MultipartFile denomination,
                                                                       MultipartFile extractRNE,
                                                                       MultipartFile pvChangeAddress,
                                                                       MultipartFile oldBusinessLicence,
                                                                       MultipartFile oldExistenceDeclaration) throws UserNotFoundException, IOException {
        DomiciliationRequest domiciliationRequest = new DomiciliationRequest();
        domiciliationRequest.setCin(domiciliationPostRequest.getCin());
        domiciliationRequest.setDenomination(domiciliationPostRequest.getDenomination());
        domiciliationRequest.setDraftStatus(domiciliationPostRequest.getDraftStatus());
        domiciliationRequest.setShareCapital(domiciliationPostRequest.getShareCapital());
        domiciliationRequest.setManagement(domiciliationPostRequest.getManagement());
        domiciliationRequest.setOldDraftStatus(domiciliationPostRequest.getOldDraftStatus());
        if(domiciliationPostRequest.getOldLegalForm().equals("Corporation")){
            domiciliationRequest.setOldLegalForm(LegalForm.CORPORATION);
        }else{
            domiciliationRequest.setOldLegalForm(LegalForm.NATURAL_PERSON);
        }
        domiciliationRequest.setOldShareCapital(domiciliationPostRequest.getOldShareCapital());
        domiciliationRequest.setOldManagement(domiciliationPostRequest.getOldManagement());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        domiciliationRequest.setClient(authenticatedUser);
        domiciliationRequest.setCompanyStatus(CompanyStatus.TRANSFER);
        domiciliationRequest.setLegalForm(LegalForm.CORPORATION);
        File savedCin = fileRepository.save(File.builder()
                .name(cin.getOriginalFilename())
                .type(cin.getContentType())
                .fileData(cin.getBytes())
                .build());

        File savedDenomination = fileRepository.save(File.builder()
                .name(denomination.getOriginalFilename())
                .type(denomination.getContentType())
                .fileData(denomination.getBytes())
                .build());

        File savedExtractRNE = fileRepository.save(File.builder()
                .name(extractRNE.getOriginalFilename())
                .type(extractRNE.getContentType())
                .fileData(extractRNE.getBytes())
                .build());

        File savedPvChangeAddress = fileRepository.save(File.builder()
                .name(pvChangeAddress.getOriginalFilename())
                .type(pvChangeAddress.getContentType())
                .fileData(pvChangeAddress.getBytes())
                .build());

        File savedOldBusinessLicence = fileRepository.save(File.builder()
                .name(oldBusinessLicence.getOriginalFilename())
                .type(oldBusinessLicence.getContentType())
                .fileData(oldBusinessLicence.getBytes())
                .build());

        File savedOldExistenceDeclaration = fileRepository.save(File.builder()
                .name(oldExistenceDeclaration.getOriginalFilename())
                .type(oldExistenceDeclaration.getContentType())
                .fileData(oldExistenceDeclaration.getBytes())
                .build());

        Pack pack = packRepository.findById(domiciliationPostRequest.getPack()).get();
        domiciliationRequest.setPack(pack);

        if(domiciliationPostRequest.getPaymentMode().equals("Quarter")){
            domiciliationRequest.setPaymentMode(PaymentMode.QUARTER);
        }else if(domiciliationPostRequest.getPaymentMode().equals("Semester")){
            domiciliationRequest.setPaymentMode(PaymentMode.SEMESTER);
        }else if(domiciliationPostRequest.getPaymentMode().equals("Annually")){
            domiciliationRequest.setPaymentMode(PaymentMode.ANNUALLY);
        }

        domiciliationRequest.setCinFile(savedCin);
        domiciliationRequest.setDenominationFile(savedDenomination);
        domiciliationRequest.setExtractRNE(savedExtractRNE);
        domiciliationRequest.setPvChangeAddress(savedPvChangeAddress);
        domiciliationRequest.setOldBusinessLicence(savedOldBusinessLicence);
        domiciliationRequest.setOldExistenceDeclaration(savedOldExistenceDeclaration);
        domiciliationRequestRepository.save(domiciliationRequest);
        return mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest);
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequests() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAll();
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        return domiciliationRequestDTOS;
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestsInProcess() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByStatusIs(DomiciliationRequestStatus.IN_PROGRESS);
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        return domiciliationRequestDTOS;
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestsAccepted() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByStatusIs(DomiciliationRequestStatus.ACCEPTED);
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        return domiciliationRequestDTOS;
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestsRejected() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByStatusIs(DomiciliationRequestStatus.REJECTED);
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        return domiciliationRequestDTOS;
    }

    @Override
    public DomiciliationRequestDTO getDomiciliationRequestById(String id) throws DomiciliationRequestNotFoundException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            return mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequestOptional.get());
        }else{
         throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String acceptDomiciliationRequestAdmin(String id, MultipartFile draftContract) throws DomiciliationRequestNotFoundException, IOException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            domiciliationRequest.setStatus(DomiciliationRequestStatus.ACCEPTED);
            ResponseDomiAdmin responseDomiAdmin = new ResponseDomiAdmin();

            File savedDraftContract = fileRepository.save(File.builder()
                    .name(draftContract.getOriginalFilename())
                    .type(draftContract.getContentType())
                    .fileData(draftContract.getBytes())
                    .build());

            responseDomiAdmin.setDraftContract(savedDraftContract);
            domiciliationRequest.setResponseDomiAdmin(responseDomiAdmin);
            responseDomiAdminRepository.save(responseDomiAdmin);
            domiciliationRequestRepository.save(domiciliationRequest);
            return "Domiciliation request accepted";
        }else {
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public ResponseDomiAdminDTO getResponseDomiAdminById(String id) throws ResponseAdminNotFoundException {
        Optional<ResponseDomiAdmin> responseDomiAdminOptional = responseDomiAdminRepository.findById(id);
        if(responseDomiAdminOptional.isPresent()){
            return mapper.fromResponseDomiAdminToResponseDomiAdminDTO(responseDomiAdminOptional.get());
        }else{
            throw new ResponseAdminNotFoundException("Admin response not found");
        }
    }

    @Override
    public String rejectDomiciliationRequestAdmin(String id) throws DomiciliationRequestNotFoundException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            domiciliationRequest.setStatus(DomiciliationRequestStatus.REJECTED);

            domiciliationRequestRepository.save(domiciliationRequest);
            return "Domiciliation request rejected";
        }else {
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String acceptContractTermsClient(String id) throws DomiciliationRequestNotFoundException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            ResponseClient responseClient = new ResponseClient();
            responseClient.setResponse(com.domiaffaire.api.enums.ClientResponse.ACCEPTED);
            responseClientRepository.save(responseClient);
            domiciliationRequest.setClientConfirmation(responseClient);
            Deadline deadline = new Deadline();
            int paymentMode = 0;
            if (domiciliationRequest.getPaymentMode()==PaymentMode.QUARTER){
                paymentMode = 3;
            }else if(domiciliationRequest.getPaymentMode()==PaymentMode.SEMESTER){
                paymentMode = 6;
            }else if(domiciliationRequest.getPaymentMode()==PaymentMode.ANNUALLY){
                paymentMode = 12;
            }
            deadline.setNetPayable(calculateNetPayable(domiciliationRequest.getPack().getPrice(),paymentMode,1));
            deadline.setCounterOfNotPaidPeriods(0);
            deadlineRepository.save(deadline);
            domiciliationRequest.setDeadline(deadline);
            domiciliationRequestRepository.save(domiciliationRequest);
            return "You have accepted the terms of the contract.";
        }else{
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public BigDecimal calculateNetPayable(float pack, int paymentMode, int timbre) {
        BigDecimal totalHTVA = BigDecimal.valueOf(pack).multiply(BigDecimal.valueOf(paymentMode));
        BigDecimal tva = totalHTVA.multiply(BigDecimal.valueOf(0.19));
        BigDecimal totalTTC = totalHTVA.add(tva);
        BigDecimal rs = totalTTC.multiply(BigDecimal.valueOf(0.1));
        BigDecimal net = totalTTC.subtract(rs).add(BigDecimal.valueOf(timbre));
        BigDecimal scaledNet = net.setScale(3, BigDecimal.ROUND_HALF_UP);
        return scaledNet;
    }

//    @Override
//    public void sendPaimentLimitWarning() {
//        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAll();
//        for (DomiciliationRequest elmt:domiciliationRequests){
//            final Object lock = new Object();
//            LocalDateTime paymentDate = elmt.getDeadline().getDateBeginig();
////            LocalDateTime limitDate = paymentDate.plusMonths(3);
//            LocalDateTime limitedDate2 = LocalDateTime.of(2024, 5, 15, 17, 49, 0);
//            Duration durationUntilLimit = Duration.between(limitedDate2,LocalDateTime.now());
//            long secondsUntilLimit = Math.max(0, durationUntilLimit.getSeconds());
//            while (true) {
//                synchronized (lock) {
//                    LocalDateTime currentDateTime = LocalDateTime.now();
//                    if (currentDateTime.compareTo(limitedDate2) > 0) {
//                        System.out.println("You passed the payment limit.");
//                        elmt.getDeadline().setCounterOfNotPaidPeriods(elmt.getDeadline().getCounterOfNotPaidPeriods()+1);
//                        elmt.getDeadline().setDateBeginig(elmt.getDeadline().getDateBeginig().plusMinutes(2));
//                        limitedDate2 = limitedDate2.plusMinutes(5);
//                        publisher.publishEvent(new RegistrationCompleteEvent(elmt.getClient()));
//                        deadlineRepository.save(elmt.getDeadline());
//                        domiciliationRequestRepository.save(elmt);
//                        try {
//                            sendEmailPaimentLimitWarning();
//                        } catch (MessagingException e) {
//                            throw new RuntimeException(e);
//                        } catch (UnsupportedEncodingException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//                try {
//                    Thread.sleep(secondsUntilLimit*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//    }

//    @Override
//    public void sendPaimentLimitWarning() {
//        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAll();
//        for (DomiciliationRequest elmt : domiciliationRequests) {
//            final Object lock = new Object();
//            LocalDateTime limitedDate2 = LocalDateTime.of(2024, 5, 15, 18, 02, 0);
//            Duration durationUntilLimit = Duration.between(limitedDate2, LocalDateTime.now());
//            long secondsUntilLimit = Math.max(0, durationUntilLimit.getSeconds());
//            while (true) {
//                synchronized (lock) {
//                    LocalDateTime currentDateTime = LocalDateTime.now();
//                    if (currentDateTime.compareTo(limitedDate2) > 0) {
//                        System.out.println("You passed the payment limit.");
//                        elmt.getDeadline().setCounterOfNotPaidPeriods(elmt.getDeadline().getCounterOfNotPaidPeriods() + 1);
//                        elmt.getDeadline().setDateBeginig(elmt.getDeadline().getDateBeginig().plusMinutes(2));
//                        limitedDate2 = limitedDate2.plusMinutes(5);
//                        publisher.publishEvent(new RegistrationCompleteEvent(elmt.getClient()));
//                        deadlineRepository.save(elmt.getDeadline());
//                        domiciliationRequestRepository.save(elmt);
//                        try {
//                            sendEmailPaimentLimitWarning();
//                        } catch (MessagingException e) {
//                            throw new RuntimeException(e);
//                        } catch (UnsupportedEncodingException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//                try {
//                    Thread.sleep(secondsUntilLimit * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        deadlineRepository.saveAll(deadlineRepository.findAll());
//        domiciliationRequestRepository.saveAll(domiciliationRequests);
//    }

    @Override
    public void sendPaimentLimitWarning() {
        while (true) {
            List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAll();
            LocalDateTime currentDateTime = LocalDateTime.now();

            for (DomiciliationRequest elmt : domiciliationRequests) {
                LocalDateTime limitedDate2 = LocalDateTime.of(2024, 5, 15, 18, 15, 0);
                if (currentDateTime.compareTo(limitedDate2) > 0) {
                    System.out.println("You passed the payment limit.");
                    elmt.getDeadline().setCounterOfNotPaidPeriods(elmt.getDeadline().getCounterOfNotPaidPeriods() + 1);
                    elmt.getDeadline().setDateBeginig(elmt.getDeadline().getDateBeginig().plusMinutes(2));
                    limitedDate2 = limitedDate2.plusMinutes(5);
                    publisher.publishEvent(new RegistrationCompleteEvent(elmt.getClient()));
                    deadlineRepository.save(elmt.getDeadline());
                    domiciliationRequestRepository.save(elmt);
                    try {
                        sendEmailPaimentLimitWarning();
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                // Sleep for a while before checking again (adjust the sleep duration as needed)
                Thread.sleep(1000); // Sleep for 1 minute (60000 milliseconds)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public void sendEmailPaimentLimitWarning() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendDeadlinePassedLimitWarning();
    }

    @Override
    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }


    @Override
    public String rejectContractTermsClient(ClientResponse clientResponse, String id) throws ResponseAdminNotFoundException {
        return null;
    }

    @Override
    public PackDTO addPack(PackRequest packRequest) {
        Pack pack = new Pack();
        pack.setDesignation(packRequest.getDesignation());
        pack.setDescription(packRequest.getDescription());
        pack.setPrice(packRequest.getPrice());
        packRepository.save(pack);
        return mapper.fromPackToPackDTO(pack);
    }

    @Override
    public PackDTO getPackById(String id) throws PackNotFoundException {
        Optional<Pack> optionalPack = packRepository.findById(id);
        if(optionalPack.isPresent()){
            return mapper.fromPackToPackDTO(optionalPack.get());
        }else{
            throw new PackNotFoundException("Pack not found");
        }
    }

    @Override
    public List<PackDTO> getAllPacks() {
        List<Pack> packs = packRepository.findAll();
        return packs.stream()
                .map(pack->mapper.fromPackToPackDTO(pack))
                .collect(Collectors.toList());
    }

    @Override
    public PackDTO updatePack(PackRequest packRequest, String id) throws PackNotFoundException {
        Optional<Pack> optionalPack = packRepository.findById(id);
        if(optionalPack.isPresent()){
            Pack pack = optionalPack.get();
            pack.setId(id);
            pack.setPrice(packRequest.getPrice());
            pack.setDescription(packRequest.getDescription());
            pack.setDesignation(packRequest.getDesignation());
            packRepository.save(pack);
            return mapper.fromPackToPackDTO(pack);
        }else{
            throw new PackNotFoundException("Pack not found");
        }
    }

    @Override
    public void deletePack(String id) throws PackNotFoundException {
        Optional<Pack> optionalPack = packRepository.findById(id);
        if(optionalPack.isPresent()){
            packRepository.delete(optionalPack.get());
        }else{
            throw new PackNotFoundException("Pack not found");
        }
    }


    @Override
    public void archiveProfile(String id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNotArchived(false);
            userRepository.save(user);
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void unarchiveProfile(String id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNotArchived(true);
            userRepository.save(user);
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        File newFile = new File();
        fileRepository.save(newFile.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .fileData(file.getBytes()).build());
        if(newFile != null)
            return "File uploaded successfully : "+file.getOriginalFilename();
        return null;
    }

    @Override
    public String uploadFileCompanyCreation(MultipartFile file) throws IOException {
        File newFile = new File();
        fileRepository.save(newFile.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .fileData(file.getBytes())
                .companyCreation(true).build());
        if(newFile != null)
            return "File uploaded successfully : "+file.getOriginalFilename();
        return null;
    }

    @Override
    public ConsultationRequestDTO addConsultationRequest(ConsultationPostRequest consultationPostRequest) throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        ConsultationRequest consultationRequest = new ConsultationRequest();
        consultationRequest.setDetails(consultationPostRequest.getDetails());
        consultationRequest.setBudget(consultationPostRequest.getBudget());
        consultationRequest.setDraftStatus(consultationPostRequest.getDraftStatus());
        consultationRequest.setSubject(consultationPostRequest.getSubject());
        consultationRequest.setSentAt(LocalDateTime.now());
        consultationRequest.setProposedDate(consultationPostRequest.getProposedDate());
        consultationRequest.setSentBy(authenticatedUser);
        consultationRequestRepository.save(consultationRequest);
        return mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest);
    }




    @Override
    public UserDTO updateUser(UpdateProfileRequest updateProfileRequest, byte[] imageBytes, String role, String id) {
        if (role.equals(UserRole.CLIENT.toString())) {
            User client = userRepository.findById(id).get();
            client.setId(id);
            client.setEmail(client.getEmail());
            client.setFirstName(updateProfileRequest.getFirstName());
            client.setLastName(updateProfileRequest.getLastName());
            client.setImage(imageBytes);
            client.setPwd(client.getPwd());
            client.setPhoneNumber(updateProfileRequest.getPhoneNumber());
            client.setBirthDate(updateProfileRequest.getBirthDate());
            client.setUserRole(UserRole.CLIENT);
            User createdClient = userRepository.save(client);
            UserDTO createdClientDto = new UserDTO();
            BeanUtils.copyProperties(createdClient, createdClientDto);
            return createdClientDto;
        } else if (role.equals(UserRole.ACCOUNTANT.toString())) {
            User comptable =userRepository.findById(id).get();
            comptable.setId(id);
            comptable.setFirstName(updateProfileRequest.getFirstName());
            comptable.setLastName(updateProfileRequest.getLastName());
            comptable.setImage(imageBytes);
            comptable.setPwd(comptable.getPwd());
            comptable.setPhoneNumber(updateProfileRequest.getPhoneNumber());
            comptable.setBirthDate(updateProfileRequest.getBirthDate());
            comptable.setUserRole(UserRole.ACCOUNTANT);
            User createdComptable = userRepository.save(comptable);
            UserDTO createdcomptableDto = new UserDTO();
            BeanUtils.copyProperties(createdComptable, createdcomptableDto);
            return createdcomptableDto;
        } else {
            return null;
        }
    }

    @Override
    public UserDTO updateAdmin(UpdateAdminProfileRequest updateAdminProfileRequest, byte[] imageBytes, String id) {
        User admin = userRepository.findById(id).get();
        if(admin!=null){
            admin.setId(id);
            admin.setEmail(admin.getEmail());
            admin.setPwd(admin.getPwd());
            admin.setName(updateAdminProfileRequest.getUsername());
            admin.setImage(imageBytes);
            admin.setUserRole(UserRole.ADMIN);
            User createdAdmin = userRepository.save(admin);
            UserDTO createdAdminDTO = new UserDTO();
            BeanUtils.copyProperties(createdAdmin,createdAdminDTO);
            return createdAdminDTO;
        }
        return null;
    }


    @Override
    public FileDTO renameFile(RenameFileRequest renameFileRequest, String id) throws FileNotFoundException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            File savedFile = fileOptional.get();
            String currentExtension = savedFile.getType().substring(savedFile.getType().lastIndexOf("/") + 1);
            String newName = renameFileRequest.getName();
            if (newName.contains(".")) {
                String providedExtension = newName.substring(newName.lastIndexOf(".") + 1);
                if (!providedExtension.equalsIgnoreCase(currentExtension)) {
                    newName = newName.substring(0, newName.lastIndexOf(".")) + "." + currentExtension;
                }
            } else {
                newName += "." + currentExtension;
            }

            savedFile.setName(newName);
            fileRepository.save(savedFile);
            return mapper.fromFileToFileDTO(savedFile);
        } else {
            throw new FileNotFoundException("File not found");
        }
    }

    @Override
    public FileDTO updateFile(MultipartFile file, String id) throws FileNotFoundException, IOException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            File savedFile = fileOptional.get();
            savedFile.setFileData(file.getBytes());
            savedFile.setName(file.getOriginalFilename());
            savedFile.setType(file.getContentType());
            fileRepository.save(savedFile);
            return mapper.fromFileToFileDTO(savedFile);
        } else {
            throw new FileNotFoundException("File not found");
        }
    }




    @Override
    public void changePassword(String id, ChangePasswordRequest changePasswordRequest) throws WrongPasswordException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setId(id);
            boolean passwordMatches = new BCryptPasswordEncoder().matches(changePasswordRequest.getOldPassword(), user.getPassword());
            if(!passwordMatches){
                throw new WrongPasswordException("False old password");
            }else{
                if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())){
                    throw new WrongPasswordException("Passwords does not match");
                }else{
                    user.setPwd(new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword()));
                    userRepository.save(user);
                }
            }

        }
    }



}
