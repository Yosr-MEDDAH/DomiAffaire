package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.NoContentException;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.exceptions.WrongPasswordException;

import java.util.List;

public interface    DomiAffaireService {
    List<UserDTO> findAllUsers();
    void changePassword(String id, ChangePasswordRequest changePasswordRequest) throws WrongPasswordException;
    List<ClientDTO> findAllClientsArchived() throws NoContentException;
    List<ClientDTO> findAllClients() throws NoContentException;
    List<ComptableDTO> findAllComptables() throws NoContentException;
    User findUserByEmail(String email) throws UserNotFoundException;
    UserDTO updateUser(UpdateProfileRequest updateProfileRequest, byte[] imageBytes,String role, String id);
    UserDTO updateAdmin(UpdateAdminProfileRequest updateAdminProfileRequest, byte[] imageBytes, String id );
    void archiveProfile(String id) throws UserNotFoundException;
    void unarchiveProfile(String id) throws UserNotFoundException;
}
