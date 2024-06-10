package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.*;
import com.domiaffaire.api.exceptions.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface DomiAffaireService {
    List<UserDTO> findAllUsers();
    void changePassword(String id, ChangePasswordRequest changePasswordRequest) throws WrongPasswordException;
    List<ClientDTO> findAllClientsArchived() throws NoContentException;
    List<ClientDTO> findAllClients() throws NoContentException;
    List<AccountantDTO> findAllAccountants() throws NoContentException;
    User findUserByEmail(String email) throws UserNotFoundException;
    UserDTO updateUser(UpdateProfileRequest updateProfileRequest, byte[] imageBytes,String role, String id);
    UserDTO updateAdmin(UpdateAdminProfileRequest updateAdminProfileRequest, byte[] imageBytes, String id );
    void archiveProfile(String id) throws UserNotFoundException;
    void unarchiveProfile(String id) throws UserNotFoundException;
    String uploadFile(MultipartFile file)throws IOException;
    String uploadFileCompanyCreation(MultipartFile file)throws IOException;
    FileDTO renameFile(RenameFileRequest renameFileRequest, String id) throws FileNotFoundException;
    FileDTO getFileById(String id) throws FileNotFoundException;
    FileDTO updateFile(MultipartFile file, String id) throws FileNotFoundException, IOException;
    List<FileDTO> findAllFiles();
    List<FileDTO> findAllFilesCompanyCreation();
    void deleteFile(String id) throws FileNotFoundException;
    ConsultationRequestDTO addConsultationRequest(ConsultationPostRequest consultationPostRequest) throws UserNotFoundException;
    List<ConsultationRequestDTO> findAllConsultationsByClient() throws UserNotFoundException;
    List<DomiciliationRequestDTO> findAllDomiciliationRequestByClient() throws UserNotFoundException;
    List<DomiciliationRequestDTO> findAllDomiciliationDocuments();
    List<ConsultationRequestDTO> findAllConsultationRequests();
    List<ConsultationRequestDTO> findAllConsultationsInProgress();
    List<ConsultationRequestDTO> findAllConsultationsAccepted();
    List<ConsultationRequestDTO> findAllConsultationsRejected();
    List<ConsultationRequestDTO> findAllConsultationsAcceptedOrRejected();
    void cancelConsultationRequest(String id) throws ConsultationRequestNotFoundException;
    void cancelDomiciliationRequest(String id) throws DomiciliationRequestNotFoundException;
    ChatDTO validateConsultationRequest(String id) throws UserNotFoundException, ConsultationRequestNotFoundException;
    String rejectConsultationRequest(String id) throws UserNotFoundException, ConsultationRequestNotFoundException;

    ChatMessage sendMessage(String chatId, ChatMessage chatMessage) throws ChatNotFoundException, UserNotFoundException;
    ChatDTO createChat(String clientId, String accountantId) throws UserNotFoundException;
    List<ChatDTO> getAllChatsByAuthenticatedUserClient() throws UserNotFoundException;
    List<ChatDTO> getAllChatsByAuthenticatedUserAccountant() throws UserNotFoundException;
    List<Message> getAllMessagesByChat(String id);
    ChatDTO getChatById(String id) throws ChatNotFoundException;
    DomiciliationRequestDTO sendDomiciliationRequestPP(DomiciliationPostRequest domiciliationPostRequest, MultipartFile cin,MultipartFile denomination) throws UserNotFoundException,IOException;
    DomiciliationRequestDTO sendDomiciliationRequestPMInProcess(DomiciliationPostRequest domiciliationPostRequest, MultipartFile cin,MultipartFile denomination, MultipartFile extractRNE) throws UserNotFoundException,IOException;
    DomiciliationRequestDTO sendDomiciliationRequestPMTransfert(DomiciliationPostRequest domiciliationPostRequest, MultipartFile cin,MultipartFile denomination, MultipartFile extractRNE, MultipartFile pvChangeAddress, MultipartFile oldBusinessLicence, MultipartFile oldExistenceDeclaration) throws UserNotFoundException,IOException;
    List<DomiciliationRequestDTO> findAllDomiciliationRequests() throws UserNotFoundException;
    List<DomiciliationRequestDTO> findAllDomiciliationRequestsInProcess();
    List<DomiciliationRequestDTO> findAllDomiciliationRequestsAccepted();
    List<DomiciliationRequestDTO> findAllDomiciliationRequestsRejected();
    DomiciliationRequestDTO getDomiciliationRequestById(String id) throws DomiciliationRequestNotFoundException;
    String acceptDomiciliationRequestAdmin(String id, MultipartFile draftContract) throws DomiciliationRequestNotFoundException, IOException, UserNotFoundException;


    String addPatenteFileToDomiciliation(String id,MultipartFile businessLicence) throws DomiciliationRequestNotFoundException, IOException;
    String addContractFileToDomiciliation(String id,MultipartFile contract) throws DomiciliationRequestNotFoundException, IOException;
    String addExistenceDeclaration(String id,MultipartFile existenceDeclaration) throws DomiciliationRequestNotFoundException, IOException;

    ResponseDomiAdminDTO getResponseDomiAdminById(String id) throws ResponseAdminNotFoundException;
    String rejectDomiciliationRequestAdmin(String id) throws DomiciliationRequestNotFoundException;
    String acceptContractTermsClient(String id)throws  DomiciliationRequestNotFoundException;
    String protestContractTermsClient(String id, ClientResponse clientResponse)throws  DomiciliationRequestNotFoundException;

    BigDecimal calculateNetPayable(float pack, int modePaiment, int timbre);

    PackDTO addPack(PackRequest packRequest);
    PackDTO getPackById(String id) throws PackNotFoundException;
    List<PackDTO> getAllPacks();
    PackDTO updatePack(PackRequest packRequest,String id) throws PackNotFoundException;
    void deletePack(String id) throws PackNotFoundException;

    BlogCategoryDTO addBlogCategory(BlogCategoryRequest blogCategoryRequest);
    BlogCategoryDTO getBlogCategoryById(String id)throws BlogCategoryNotFoundException;
    List<BlogCategoryDTO> getAllBlogCategories();
    BlogCategoryDTO updateBlogCategory(BlogCategoryRequest blogCategoryRequest, String id) throws BlogCategoryNotFoundException;
    void deleteBlogCategory(String id)throws BlogCategoryNotFoundException;

    BlogDTO addBlog(BlogRequest blogRequest,MultipartFile image) throws UserNotFoundException, IOException,BlogCategoryNotFoundException;
    BlogDTO getBlogById(String id) throws BlogNotFoundException;
    List<BlogDTO> getAllBlogs();
    List<BlogDTO> getAllBlogsNotArchived();
    List<BlogDTO> getAllBlogsByCategory(String id) throws BlogCategoryNotFoundException;
    BlogDTO updateBlog(BlogRequest blogRequest, String id,MultipartFile image) throws BlogNotFoundException,IOException,BlogCategoryNotFoundException;
    void deleteBlog(String id) throws BlogNotFoundException;
    List<BlogDTO> getAllBlogsByAuthenticatedAdmin() throws UserNotFoundException;
    List<BlogDTO> getAllBlogsByAuthenticatedAccountant() throws UserNotFoundException;
    String archiveBlog(String id) throws BlogNotFoundException, UserNotFoundException;
    String desarchiveBlog(String id) throws BlogNotFoundException, UserNotFoundException;
    Set<BlogDTO> getAllBlogsArchivedByUser() throws UserNotFoundException;


    FaqDTO addFaq(FaqRequest faqRequest);
    FaqDTO getFaqById(String id) throws FaqNotFoundException;
    List<FaqDTO> getAllFaqs();
    FaqDTO updateFaq(FaqRequest faqRequest, String id) throws FaqNotFoundException;
    void deleteFaq(String id) throws FaqNotFoundException;

    List<Blog> saveBlogForClient(String id) throws BlogNotFoundException, UserNotFoundException, BlogAlreadyExistsException;
    BlogDTO unsaveBlogForClient(String id) throws BlogNotFoundException, UserNotFoundException, BlogDoesNotExistException;
    List<BlogDTO>getAllSavedBlogsByClient() throws UserNotFoundException;


    void deleteAccount(String email) throws UserNotFoundException;
    String updateNetPayable(String id, UpdateNetPayableRequest updateNetPayableRequest) throws DeadlineNotFoundException, DomiciliationRequestNotFoundException;

    List<PackCountDTO> getPackCounts();

    List<CapitalSocialDTO> getCapitalSocialDistribution();
    String getCapitalRange(DomiciliationRequest request);



}
