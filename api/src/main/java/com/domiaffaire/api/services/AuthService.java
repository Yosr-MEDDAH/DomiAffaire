package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.SignupRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.WrongCodeAccountantException;

import java.util.Optional;

public interface AuthService {
    User createUser(SignupRequest signupRequest,byte[] imageBytes) throws WrongCodeAccountantException;
    boolean hasUserWithEmail(String email);
    void createPasswordResetTokenForUser(User user, String passwordToken);
    String validatePasswordResetToken(String theToken);
    Optional<User> findUserByPasswordToken(String passwordToken);
}
