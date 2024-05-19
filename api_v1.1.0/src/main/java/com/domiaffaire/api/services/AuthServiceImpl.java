package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.SignupRequest;
import com.domiaffaire.api.entities.PasswordResetToken;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
import com.domiaffaire.api.exceptions.WrongCodeAccountantException;
import com.domiaffaire.api.repositories.PasswordResetTokenRepository;
import com.domiaffaire.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private  final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User createUser(SignupRequest signupRequest, byte[] imageBytes) throws WrongCodeAccountantException {
        if (signupRequest.getRole().equals(UserRole.CLIENT.toString())) {
            User client = new User();
            client.setEmail(signupRequest.getEmail());
            client.setFirstName(signupRequest.getFirstName());
            client.setLastName(signupRequest.getLastName());
            client.setPwd(new BCryptPasswordEncoder().encode(signupRequest.getPwd()));
            client.setImage(imageBytes);
            client.setPhoneNumber(signupRequest.getPhoneNumber());
            client.setBirthDate(signupRequest.getBirthDate());
            client.setUserRole(UserRole.CLIENT);
            User createdClient = userRepository.save(client);
            User createdUser = new User();
            BeanUtils.copyProperties(createdClient, createdUser);
            return createdUser;
        } else if (signupRequest.getRole().equals(UserRole.ACCOUNTANT.toString())) {
            User comptable = new User();
            comptable.setEmail(signupRequest.getEmail());
            comptable.setFirstName(signupRequest.getFirstName());
            comptable.setLastName(signupRequest.getLastName());
            comptable.setPwd(new BCryptPasswordEncoder().encode(signupRequest.getPwd()));
            comptable.setImage(imageBytes);
            comptable.setPhoneNumber(signupRequest.getPhoneNumber());
            comptable.setCode(signupRequest.getCode());
            comptable.setUserRole(UserRole.ACCOUNTANT);
            if ("abcd".equals(signupRequest.getCode())) {
                User createdAccountant = userRepository.save(comptable);
                User createdUser = new User();
                BeanUtils.copyProperties(createdAccountant, createdUser);
                return createdUser;
            } else {
                throw new WrongCodeAccountantException("Code accountant does not match");
            }
        } else {
            return null;
        }
    }


    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken,user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public Optional<User> findUserByPasswordToken(String passwordToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordToken).getUser());
    }

    @Override
    public String validatePasswordResetToken(String theToken) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid Password Reset Token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getExpirationTime().getTime() - calendar.getTime().getTime())<=0){
            passwordResetTokenRepository.delete(token);
            return "Link already expired, resend link";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }


}
