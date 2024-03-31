package com.domiaffaire.api.services.jwt;

import com.domiaffaire.api.entities.PasswordResetToken;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.entities.VerificationToken;
import com.domiaffaire.api.repositories.PasswordResetTokenRepository;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.repositories.VerificationTokenRepository;
import com.domiaffaire.api.services.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private  final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AuthServiceImpl authService;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findFirstByEmail(username)
                        .map(User::new)
                        .orElseThrow(()->new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public void saveUserVerificationToken(User user, String token) {
        var verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String verificationToken) {
        VerificationToken token = verificationTokenRepository.findByToken(verificationToken);
        if(token == null){
            return "Invalid Verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getExpirationTime().getTime() - calendar.getTime().getTime())<=0){
            verificationTokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        authService.createPasswordResetTokenForUser(user, passwordToken);
    }

    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        return authService.validatePasswordResetToken(passwordResetToken);
    }

    @Override
    public User findUserByPasswordToken(String passwordResetToken) {
        return authService.findUserByPasswordToken(passwordResetToken).get();
    }

    @Override
    public void resetUserPassword(User user, String newPassword) {
        user.setPwd(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    }
}
