package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.ClientDTO;
import com.domiaffaire.api.dto.SignupRequest;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.Client;
import com.domiaffaire.api.entities.PasswordResetToken;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
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
    public UserDTO createUser(SignupRequest signupRequest) {
        Client user = new Client();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPwd(new BCryptPasswordEncoder().encode(signupRequest.getPwd()));
        user.setImage(signupRequest.getImage());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setImage(signupRequest.getImage());
        user.setBirthDate(signupRequest.getBirthDate());
        user.setUserRole(UserRole.USER);
        User createdUser =userRepository.save(user);
        UserDTO createdUserDto = new ClientDTO();
        BeanUtils.copyProperties(createdUser, createdUserDto);
        return createdUserDto;
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
