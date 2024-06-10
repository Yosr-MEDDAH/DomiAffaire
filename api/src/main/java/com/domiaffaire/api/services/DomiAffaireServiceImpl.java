package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.dto.ClientResponse;
import com.domiaffaire.api.entities.*;
import com.domiaffaire.api.enums.*;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.exceptions.*;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DomiAffaireServiceImpl implements DomiAffaireService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final Mapper mapper;
    private final DomiciliationRequestRepository domiciliationRequestRepository;
    private final PackRepository packRepository;
    private final ResponseDomiAdminRepository responseDomiAdminRepository;
    private final ResponseClientRepository responseClientRepository;
    private final BlogRepository blogRepository;
    private final BlogCategoryRepository blogCategoryRepository;
    private final FaqRepository faqRepository;
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
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllBySentByOrderBySentAtDesc(authenticatedUser);
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
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByClientAndDeadlineIsNullOrderByCreatedAtDesc(authenticatedUser);
        authenticatedUser.setDomiciliationNotificationCount(0);
        userRepository.save(authenticatedUser);
        return domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationDocuments() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByDocumentCodeIsNotNull();
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
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIsOrderBySentAtDesc(ConsultationStatus.IN_PROGRESS);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsAccepted() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIsOrderBySentAtDesc(ConsultationStatus.ACCEPTED);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsRejected() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIsOrderBySentAtDesc(ConsultationStatus.REJECTED);
        return consultationRequests.stream()
                .map(consultationRequest -> mapper.fromConsultationRequestToConsultationRequestDTO(consultationRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationRequestDTO> findAllConsultationsAcceptedOrRejected() {
        List<ConsultationRequest> consultationRequests = consultationRequestRepository.findAllByStatusIsOrStatusIsOrderBySentAtDesc(ConsultationStatus.ACCEPTED,ConsultationStatus.REJECTED);
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
    public ChatDTO validateConsultationRequest(String id) throws UserNotFoundException, ConsultationRequestNotFoundException {
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
        List<Chat> chats = chatRepository.findAll();
        boolean chatDuplicated = false;
        ChatDTO chatDTOExist = new ChatDTO();
        for (Chat chat:chats){
            if(chat.getClient().getId().equals(clientId) && chat.getAccountant().getId().equals(accountantId)){
                chatDTOExist = mapper.fromChatToChatDTO(chat);
                chatDuplicated = true;
            }
        }
        if(chatDuplicated){
            return chatDTOExist;
        }else{
            ChatDTO createdChatDto = createChat(clientId,accountantId);
            return createdChatDto;
        }
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
    public List<ChatDTO> getAllChatsByAuthenticatedUserClient() throws UserNotFoundException {
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
    public List<ChatDTO> getAllChatsByAuthenticatedUserAccountant() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<Chat> chats = chatRepository.findAllByAccountantIs(authenticatedUser);
        List<ChatDTO> chatDTOS = chats.stream()
                .map(chat -> mapper.fromChatToChatDTO(chat))
                .collect(Collectors.toList());
        return chatDTOS;
    }

    @Override
    public List<Message> getAllMessagesByChat(String id) {
        Chat chat = chatRepository.findById(id).get();
        List<Message> messages = chat.getMessages();
        return messages;
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
                .size(cin.getSize())
                .build());

        File savedDenomination = fileRepository.save(File.builder()
                .name(denomination.getOriginalFilename())
                .type(denomination.getContentType())
                .fileData(denomination.getBytes())
                .size(denomination.getSize())
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
        domiciliationRequest.calculateDocumentsSize();
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
                .size(cin.getSize())
                .build());

        File savedDenomination = fileRepository.save(File.builder()
                .name(denomination.getOriginalFilename())
                .type(denomination.getContentType())
                .fileData(denomination.getBytes())
                        .size(denomination.getSize())
                .build());

        File savedExtractRNE = fileRepository.save(File.builder()
                .name(extractRNE.getOriginalFilename())
                .type(extractRNE.getContentType())
                .fileData(extractRNE.getBytes())
                        .size(extractRNE.getSize())
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
        domiciliationRequest.calculateDocumentsSize();
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
                        .size(cin.getSize())
                .build());

        File savedDenomination = fileRepository.save(File.builder()
                .name(denomination.getOriginalFilename())
                .type(denomination.getContentType())
                .fileData(denomination.getBytes())
                        .size(denomination.getSize())
                .build());

        File savedExtractRNE = fileRepository.save(File.builder()
                .name(extractRNE.getOriginalFilename())
                .type(extractRNE.getContentType())
                .fileData(extractRNE.getBytes())
                        .size(extractRNE.getSize())
                .build());

        File savedPvChangeAddress = fileRepository.save(File.builder()
                .name(pvChangeAddress.getOriginalFilename())
                .type(pvChangeAddress.getContentType())
                .fileData(pvChangeAddress.getBytes())
                        .size(pvChangeAddress.getSize())
                .build());

        File savedOldBusinessLicence = fileRepository.save(File.builder()
                .name(oldBusinessLicence.getOriginalFilename())
                .type(oldBusinessLicence.getContentType())
                .fileData(oldBusinessLicence.getBytes())
                        .size(oldBusinessLicence.getSize())
                .build());

        File savedOldExistenceDeclaration = fileRepository.save(File.builder()
                .name(oldExistenceDeclaration.getOriginalFilename())
                .type(oldExistenceDeclaration.getContentType())
                .fileData(oldExistenceDeclaration.getBytes())
                        .size(oldExistenceDeclaration.getSize())
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
        domiciliationRequest.calculateDocumentsSize();

        domiciliationRequestRepository.save(domiciliationRequest);
        return mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest);
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequests() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAll();
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        authenticatedUser.setDomiciliationNotificationCount(0);
        userRepository.save(authenticatedUser);
        return domiciliationRequestDTOS;
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestsInProcess() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByStatusIsOrderByCreatedAtDesc(DomiciliationRequestStatus.IN_PROGRESS);
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        return domiciliationRequestDTOS;
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestsAccepted() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByStatusIsOrderByCreatedAtDesc(DomiciliationRequestStatus.ACCEPTED);
        List<DomiciliationRequestDTO> domiciliationRequestDTOS = domiciliationRequests.stream()
                .map(domiciliationRequest -> mapper.fromDomiciliationRequestToDomiciliationRequestDTO(domiciliationRequest))
                .collect(Collectors.toList());
        return domiciliationRequestDTOS;
    }

    @Override
    public List<DomiciliationRequestDTO> findAllDomiciliationRequestsRejected() {
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByStatusIsOrderByCreatedAtDesc(DomiciliationRequestStatus.REJECTED);
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
    public String acceptDomiciliationRequestAdmin(String id, MultipartFile draftContract) throws DomiciliationRequestNotFoundException, IOException, UserNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
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
            responseDomiAdmin.setResponsedBy(authenticatedUser);
            domiciliationRequest.setResponseDomiAdmin(responseDomiAdmin);
            domiciliationRequest.getClient().setDomiciliationNotificationCount(domiciliationRequest.getClient().getDomiciliationNotificationCount()+1);
            userRepository.save(domiciliationRequest.getClient());
            responseDomiAdminRepository.save(responseDomiAdmin);
            domiciliationRequest.calculateDocumentsSize();
            domiciliationRequestRepository.save(domiciliationRequest);
            return "Domiciliation request accepted";
        }else {
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String addPatenteFileToDomiciliation(String id, MultipartFile businessLicence) throws DomiciliationRequestNotFoundException, IOException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()) {
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            if(domiciliationRequest.getBusinessLicence()!=null){
                fileRepository.delete(domiciliationRequest.getBusinessLicence());
            }
            File savedBusinessLicence = fileRepository.save(File.builder()
                    .name(businessLicence.getOriginalFilename())
                    .type(businessLicence.getContentType())
                    .fileData(businessLicence.getBytes())
                            .size(businessLicence.getSize())
                    .build());

            domiciliationRequest.setBusinessLicence(savedBusinessLicence);
            domiciliationRequest.calculateDocumentsSize();
            domiciliationRequestRepository.save(domiciliationRequest);
            return "Business license added successfully";
        }else {
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String addContractFileToDomiciliation(String id, MultipartFile contract) throws DomiciliationRequestNotFoundException, IOException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()) {
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            if(domiciliationRequest.getContract()!=null){
                fileRepository.delete(domiciliationRequest.getContract());
            }
            File savedContract = fileRepository.save(File.builder()
                    .name(contract.getOriginalFilename())
                    .type(contract.getContentType())
                    .fileData(contract.getBytes())
                            .size(contract.getSize())
                    .build());

            domiciliationRequest.setContract(savedContract);
            domiciliationRequest.calculateDocumentsSize();
            domiciliationRequestRepository.save(domiciliationRequest);
            return "Contract added successfully";
        }else {
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String addExistenceDeclaration(String id, MultipartFile existenceDeclaration) throws DomiciliationRequestNotFoundException, IOException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()) {
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            if(domiciliationRequest.getExistenceDeclaration()!=null){
                fileRepository.delete(domiciliationRequest.getExistenceDeclaration());
            }
            File savedExistenceDeclaration = fileRepository.save(File.builder()
                    .name(existenceDeclaration.getOriginalFilename())
                    .type(existenceDeclaration.getContentType())
                    .fileData(existenceDeclaration.getBytes())
                            .size(existenceDeclaration.getSize())
                    .build());


            domiciliationRequest.setExistenceDeclaration(savedExistenceDeclaration);
            domiciliationRequest.calculateDocumentsSize();
            domiciliationRequestRepository.save(domiciliationRequest);
            return "Existence declaration added successfully";
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
            domiciliationRequest.getClient().setDomiciliationNotificationCount(domiciliationRequest.getClient().getDomiciliationNotificationCount()+1);
            userRepository.save(domiciliationRequest.getClient());
            domiciliationRequest.calculateDocumentsSize();
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
                deadline.setLimitedDate(deadline.getDateBeginig().plusMonths(3));
                paymentMode = 3;
            }else if(domiciliationRequest.getPaymentMode()==PaymentMode.SEMESTER){
                deadline.setLimitedDate(deadline.getDateBeginig().plusMonths(6));
                paymentMode = 6;
            }else if(domiciliationRequest.getPaymentMode()==PaymentMode.ANNUALLY){
                deadline.setLimitedDate(deadline.getDateBeginig().plusMonths(12));
                paymentMode = 12;
            }
            domiciliationRequest.getPack().getUsers().add(domiciliationRequest.getClient());
            domiciliationRequest.getPack().setUserCount(domiciliationRequest.getPack().getUsers().size());
            packRepository.save(domiciliationRequest.getPack());
            domiciliationRequest.getResponseDomiAdmin().getResponsedBy().setDomiciliationNotificationCount(domiciliationRequest.getResponseDomiAdmin().getResponsedBy().getDomiciliationNotificationCount()+1);
            userRepository.save(domiciliationRequest.getResponseDomiAdmin().getResponsedBy());
            deadline.setPackPrice(domiciliationRequest.getPack().getPrice());
            deadline.setNetPayable(calculateNetPayable(domiciliationRequest.getPack().getPrice(),paymentMode,1));
            deadline.setCounterOfNotPaidPeriods(0);
            deadlineRepository.save(deadline);
            domiciliationRequest.setDeadline(deadline);
            String clientCode = domiciliationRequest.getClient().getFirstName()+"_"+domiciliationRequest.getClient().getLastName()+"_"+LocalDateTime.now();
            domiciliationRequest.setDocumentCode(clientCode);
            domiciliationRequestRepository.save(domiciliationRequest);
            return "You have accepted the terms of the contract.";
        }else{
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public String protestContractTermsClient(String id, ClientResponse clientResponse) throws DomiciliationRequestNotFoundException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            if(domiciliationRequestOptional.get().getProtestCount()>=2){
//                domiciliationRequestRepository.delete(domiciliationRequestOptional.get());
                domiciliationRequestOptional.get().setStatus(DomiciliationRequestStatus.REJECTED);
                domiciliationRequestOptional.get().calculateDocumentsSize();
                domiciliationRequestRepository.save(domiciliationRequestOptional.get());
                return "saye maadch njmou ne9blou hata protest sakarna";
            }else{
                DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
                ResponseClient responseClient = new ResponseClient();
                responseClient.setResponse(com.domiaffaire.api.enums.ClientResponse.REJECTED);
                responseClient.setObjectionArgument(clientResponse.getObjectionArgument());
                responseClientRepository.save(responseClient);
                domiciliationRequest.setClientConfirmation(responseClient);
                domiciliationRequest.getResponseDomiAdmin().getResponsedBy().setDomiciliationNotificationCount(domiciliationRequest.getResponseDomiAdmin().getResponsedBy().getDomiciliationNotificationCount()+1);
                domiciliationRequest.setProtestCount(domiciliationRequest.getProtestCount()+1);
                userRepository.save(domiciliationRequest.getResponseDomiAdmin().getResponsedBy());
                domiciliationRequest.calculateDocumentsSize();
                domiciliationRequestRepository.save(domiciliationRequest);
                return "Your response is sent to the administration successfully";
            }
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
    public BlogCategoryDTO addBlogCategory(BlogCategoryRequest blogCategoryRequest) {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setName(blogCategoryRequest.getName());
        blogCategoryRepository.save(blogCategory);
        return mapper.fromBlogCategoryToBlogCategoryDTO(blogCategory);
    }

    @Override
    public BlogCategoryDTO getBlogCategoryById(String id) throws BlogCategoryNotFoundException {
        Optional<BlogCategory> blogCategoryOptional = blogCategoryRepository.findById(id);
        if(blogCategoryOptional.isPresent()){
            return mapper.fromBlogCategoryToBlogCategoryDTO(blogCategoryOptional.get());
        }else{
            throw new BlogCategoryNotFoundException("Blog category not found");
        }
    }

    @Override
    public List<BlogCategoryDTO> getAllBlogCategories() {
        List<BlogCategory> blogCategories = blogCategoryRepository.findAllByOrderByCreatedAtDesc();
        return blogCategories.stream()
                .map(blogCategory->mapper.fromBlogCategoryToBlogCategoryDTO(blogCategory))
                .collect(Collectors.toList());
    }

    @Override
    public BlogCategoryDTO updateBlogCategory(BlogCategoryRequest blogCategoryRequest, String id) throws BlogCategoryNotFoundException {
        Optional<BlogCategory> optionalBlogCategory = blogCategoryRepository.findById(id);
        if(optionalBlogCategory.isPresent()){
            BlogCategory category = new BlogCategory();
            category.setId(id);
            category.setName(blogCategoryRequest.getName());
            blogCategoryRepository.save(category);
            return mapper.fromBlogCategoryToBlogCategoryDTO(category);
        }else{
            throw new BlogCategoryNotFoundException("Blog category not found");
        }
    }

    @Override
    public void deleteBlogCategory(String id) throws BlogCategoryNotFoundException {
        Optional<BlogCategory> optionalBlogCategory = blogCategoryRepository.findById(id);
        if(optionalBlogCategory.isPresent()){
            List<Blog> blogs = blogRepository.findAllByCategoryIsAndIsArchivedFalse(optionalBlogCategory.get());
            if(blogs.size()!=0){
                BlogCategory defaultCategory = blogCategoryRepository.findByName("Default").get();
                    for (Blog elmt:blogs){
                        elmt.setCategory(defaultCategory);
                        blogRepository.save(elmt);
                    }

            }
            blogCategoryRepository.delete(optionalBlogCategory.get());
        }else{
            throw new BlogCategoryNotFoundException("Blog category not found");
        }
    }

    @Override
    public BlogDTO addBlog(BlogRequest blogRequest, MultipartFile image) throws UserNotFoundException, IOException,BlogCategoryNotFoundException {
        Blog blog = new Blog();
        if(blogRequest.getCategory()!=null){
            Optional<BlogCategory> categoryOptional = blogCategoryRepository.findById(blogRequest.getCategory());
            if(categoryOptional.isPresent()){
                blog.setCategory(categoryOptional.get());
            }else{
                throw new BlogCategoryNotFoundException("Blog category not found");
            }

        }else{
            BlogCategory category = blogCategoryRepository.findByName("Default").get();
            blog.setCategory(category);
        }
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        blog.setCreatedBy(authenticatedUser);

        File savedImage = fileRepository.save(File.builder()
                .name(image.getOriginalFilename())
                .type(image.getContentType())
                .fileData(image.getBytes())
                .companyCreation(false)
                        .size(image.getSize())
                .build());

        blog.setImage(savedImage);
        blogRepository.save(blog);
        return mapper.fromBlogToBlogDTO(blog);
    }

    @Override
    public BlogDTO getBlogById(String id) throws BlogNotFoundException {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(blogOptional.isPresent()){
            return mapper.fromBlogToBlogDTO(blogOptional.get());
        }else{
            throw new BlogNotFoundException("Blog not found");
        }
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAllByOrderByCreatedAtDesc();
        return blogs.stream()
                .map(blog->mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDTO> getAllBlogsNotArchived() {
        List<Blog> blogs = blogRepository.findAllByIsArchivedFalseOrderByCreatedAtDesc();
        return blogs.stream()
                .map(blog->mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDTO> getAllBlogsByCategory(String id) throws BlogCategoryNotFoundException {
        BlogCategory  blogCategory = blogCategoryRepository.findById(id).orElseThrow(()->new BlogCategoryNotFoundException("Category not found"));
        List<Blog> blogs = blogRepository.findAllByCategoryIsAndIsArchivedFalse(blogCategory);
        return blogs.stream()
                .map(blog->mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toList());
    }

    @Override
    public BlogDTO updateBlog(BlogRequest blogRequest, String id,MultipartFile image) throws BlogNotFoundException,IOException,BlogCategoryNotFoundException {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isPresent()){
            Blog blog = optionalBlog.get();
            blog.setId(id);
            blog.setTitle(blogRequest.getTitle());
            blog.setContent(blogRequest.getContent());
            if(blogRequest.getCategory()!=null){
                Optional<BlogCategory> categoryOptional = blogCategoryRepository.findById(blogRequest.getCategory());
                if(categoryOptional.isPresent()){
                    blog.setCategory(categoryOptional.get());
                }else{
                    throw new BlogCategoryNotFoundException("Blog category not found");
                }

            }else{
                BlogCategory category = blogCategoryRepository.findByName("Default").get();
                blog.setCategory(category);
            }

            File savedImage = fileRepository.save(File.builder()
                    .name(image.getOriginalFilename())
                    .type(image.getContentType())
                    .fileData(image.getBytes())
                    .companyCreation(false)
                            .size(image.getSize())
                    .build());
            blog.setImage(savedImage);
            blogRepository.save(blog);
            return mapper.fromBlogToBlogDTO(blog);
        }else{
            throw new BlogNotFoundException("Blog not found");
        }
    }

    @Override
    public void deleteBlog(String id) throws BlogNotFoundException {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isPresent()){
            blogRepository.delete(optionalBlog.get());
        }else{
            throw new BlogNotFoundException("Blog not found");
        }
    }

    @Override
    public FaqDTO addFaq(FaqRequest faqRequest) {
        Faq faq = new Faq();
        faq.setQuestion(faqRequest.getQuestion());
        faq.setAnswer(faqRequest.getAnswer());
        faqRepository.save(faq);
        return mapper.fromFaqToFaqDTO(faq);
    }

    @Override
    public FaqDTO getFaqById(String id) throws FaqNotFoundException {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        if(optionalFaq.isPresent()){
            return mapper.fromFaqToFaqDTO(optionalFaq.get());
        }else{
            throw new FaqNotFoundException("Faq not found");
        }
    }

    @Override
    public List<FaqDTO> getAllFaqs() {
        List<Faq> faqs = faqRepository.findAll();
        return faqs.stream()
                .map(faq->mapper.fromFaqToFaqDTO(faq))
                .collect(Collectors.toList());
    }

    @Override
    public FaqDTO updateFaq(FaqRequest faqRequest, String id) throws FaqNotFoundException {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        if(optionalFaq.isPresent()){
            Faq faq = optionalFaq.get();
            faq.setId(id);
            faq.setAnswer(faqRequest.getAnswer());
            faq.setQuestion(faqRequest.getQuestion());
            faqRepository.save(faq);
            return mapper.fromFaqToFaqDTO(faq);
        }else{
            throw new FaqNotFoundException("Faq not found");
        }
    }

    @Override
    public void deleteFaq(String id) throws FaqNotFoundException {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        if(optionalFaq.isPresent()){
            faqRepository.delete(optionalFaq.get());
        }else{
            throw new FaqNotFoundException("Faq not found");
        }
    }

    @Override
    public List<BlogDTO> getAllBlogsByAuthenticatedAdmin() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<Blog> blogs = blogRepository.findAllByCreatedByAndIsArchivedFalseOrderByCreatedAtDesc(authenticatedUser);
        List<BlogDTO> blogDTOS = blogs.stream()
                .map(blog -> mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toList());
        return blogDTOS;
    }

    @Override
    public List<BlogDTO> getAllBlogsByAuthenticatedAccountant() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<Blog> blogs = blogRepository.findAllByCreatedByAndIsArchivedFalseOrderByCreatedAtDesc(authenticatedUser);
        List<BlogDTO> blogDTOS = blogs.stream()
                .map(blog -> mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toList());
        return blogDTOS;
    }

    @Override
    public String archiveBlog(String id) throws BlogNotFoundException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isPresent()){
            Blog blog = optionalBlog.get();
            List<User> users = userRepository.findAll();
            for (User user:users){
                if(user.getSavedBlogs().contains(blog))
                    user.getSavedBlogs().remove(blog);
            }
            blog.setArchived(true);
            authenticatedUser.getArchivedBlogs().add(blog);
            userRepository.saveAll(users);
            userRepository.save(authenticatedUser);
            blogRepository.save(blog);
            return "Blog archived successfully";
        }else{
            throw new BlogNotFoundException("Blog not found exception");
        }
    }

    @Override
    public String desarchiveBlog(String id) throws BlogNotFoundException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isPresent()){
            Blog blog = optionalBlog.get();
            blog.setArchived(false);
            authenticatedUser.getArchivedBlogs().remove(blog);
            blogRepository.save(blog);
            userRepository.save(authenticatedUser);
            return "Blog unarchived successfully";
        }else{
            throw new BlogNotFoundException("Blog not found exception");
        }
    }

    @Override
    public Set<BlogDTO> getAllBlogsArchivedByUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        Set<Blog> blogs = authenticatedUser.getArchivedBlogs();
        Set<BlogDTO> blogDTOS = blogs.stream()
                .map(blog -> mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toSet());
        return blogDTOS;
    }

    @Override
    public List<Blog> saveBlogForClient(String id) throws BlogNotFoundException, UserNotFoundException, BlogAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(blogOptional.isPresent()){
            if(authenticatedUser.getSavedBlogs().contains(blogOptional.get())){
                throw new BlogAlreadyExistsException("Blog already exist");
            }else{
                authenticatedUser.getSavedBlogs().add(blogOptional.get());
                userRepository.save(authenticatedUser);
                log.info("Saved Blogs size : "+authenticatedUser.getSavedBlogs().size());
                return authenticatedUser.getSavedBlogs();
            }
        }else {
            throw new BlogNotFoundException("Blog not found exception");
        }

    }

    @Override
    public BlogDTO unsaveBlogForClient(String id) throws BlogNotFoundException, UserNotFoundException, BlogDoesNotExistException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(blogOptional.isPresent()){
            if(!authenticatedUser.getSavedBlogs().contains(blogOptional.get())){
                throw new BlogDoesNotExistException("Blog does not exist in tour saved blogs");
            }else{
                authenticatedUser.getSavedBlogs().remove(blogOptional.get());
                userRepository.save(authenticatedUser);
                log.info("Saved Blogs size : "+authenticatedUser.getSavedBlogs().size());
                return mapper.fromBlogToBlogDTO(blogOptional.get());
            }
        }else {
            throw new BlogNotFoundException("Blog not found exception");
        }
    }

    @Override
    public List<BlogDTO> getAllSavedBlogsByClient() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }

        List<Blog> blogs = authenticatedUser.getSavedBlogs();
        List<BlogDTO> blogDTOS = blogs.stream()
                .map(blog -> mapper.fromBlogToBlogDTO(blog))
                .collect(Collectors.toList());
        log.info("Size of saved blogs : "+blogDTOS.size());
        return blogDTOS;
    }

    @Override
    public void deleteAccount(String email) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findFirstByEmail(email);
        if(userOptional.isPresent()){
            userRepository.delete(userOptional.get());
        }else{
            throw new UserNotFoundException("User not found.");
        }
    }

    @Override
    public String updateNetPayable(String id, UpdateNetPayableRequest updateNetPayableRequest) throws DomiciliationRequestNotFoundException {
        Optional<DomiciliationRequest> domiciliationRequestOptional = domiciliationRequestRepository.findById(id);
        if(domiciliationRequestOptional.isPresent()){
            DomiciliationRequest domiciliationRequest = domiciliationRequestOptional.get();
            domiciliationRequest.setId(id);
            if(domiciliationRequest.getDeadline()!=null){
                domiciliationRequest.getDeadline().setPackPrice(updateNetPayableRequest.getPrice());
                int paymentMode = 0;
                if (domiciliationRequest.getPaymentMode()==PaymentMode.QUARTER){
                    paymentMode = 3;
                }else if(domiciliationRequest.getPaymentMode()==PaymentMode.SEMESTER){
                    paymentMode = 6;
                }else if(domiciliationRequest.getPaymentMode()==PaymentMode.ANNUALLY){
                    paymentMode = 12;
                }
                domiciliationRequest.getDeadline().setNetPayable(calculateNetPayable(updateNetPayableRequest.getPrice(),paymentMode,1));
                deadlineRepository.save(domiciliationRequest.getDeadline());
                domiciliationRequest.calculateDocumentsSize();
                domiciliationRequestRepository.save(domiciliationRequest);
                return "Price updated successfully";
            }else{
                throw new DomiciliationRequestNotFoundException("This domiciliation have no deadline");
            }

        }else{
            throw new DomiciliationRequestNotFoundException("Domiciliation request not found");
        }
    }

    @Override
    public List<PackCountDTO> getPackCounts() {
        List<Pack> packs = packRepository.findAll();
        List<PackCountDTO> packCounts = packs.stream()
                .map(pack -> new PackCountDTO(pack.getDesignation(),pack.getUsers().size()))
                .collect(Collectors.toList());
        return packCounts;
    }

    @Override
    public List<CapitalSocialDTO> getCapitalSocialDistribution() {
        List<DomiciliationRequest> requests = domiciliationRequestRepository.findAllByShareCapitalIsNotNull();
        Map<String, Long> distribution = requests.stream()
                .collect(Collectors.groupingBy(this::getCapitalRange, Collectors.counting()));

        return distribution.entrySet().stream()
                .map(entry -> new CapitalSocialDTO(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getCapitalRange(DomiciliationRequest request) {
        double capital = request.getShareCapital();
        if (capital <= 50000) {
            return "0-50K";
        } else if (capital <= 100000) {
            return "50K-100K";
        } else if (capital <= 200000) {
            return "100K-200K";
        } else {
            return ">200K";
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
                        .size(file.getSize())
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
