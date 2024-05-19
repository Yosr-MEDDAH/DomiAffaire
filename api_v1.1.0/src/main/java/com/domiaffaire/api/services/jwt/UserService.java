package com.domiaffaire.api.services.jwt;

import com.domiaffaire.api.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    void saveUserVerificationToken(User user, String token);
    String validateToken(String verificationToken);
    void createPasswordResetTokenForUser(User user, String passwordToken);
    String validatePasswordResetToken(String passwordResetToken);

    User findUserByPasswordToken(String passwordResetToken);

    void resetUserPassword(User user, String newPassword);
}
