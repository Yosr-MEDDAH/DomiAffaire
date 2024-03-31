package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.ClientDTO;
import com.domiaffaire.api.dto.SignupRequest;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.User;

import java.util.Optional;

public interface AuthService {
    UserDTO createUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
    void createPasswordResetTokenForUser(User user, String passwordToken);
    String validatePasswordResetToken(String theToken);
    Optional<User> findUserByPasswordToken(String passwordToken);
}
