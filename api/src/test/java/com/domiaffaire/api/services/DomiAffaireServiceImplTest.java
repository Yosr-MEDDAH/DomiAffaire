package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.ChangePasswordRequest;
import com.domiaffaire.api.dto.SignupRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
import com.domiaffaire.api.exceptions.WrongCodeAccountantException;
import com.domiaffaire.api.exceptions.WrongPasswordException;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.PasswordResetTokenRepository;
import com.domiaffaire.api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



class DomiAffaireServiceImplTest {

    //which service we want to test
    @InjectMocks
    private DomiAffaireServiceImpl domiAffaireService;

    //declare the dependecies
    @Mock
    private Mapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_change_password() throws WrongPasswordException {
        // Given
        String userId = "user123";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        User user = new User();
        user.setId(userId);
        user.setPwd(new BCryptPasswordEncoder().encode(oldPassword)); // Encrypt old password

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword(newPassword);
        changePasswordRequest.setConfirmPassword(newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved user

        // When
        domiAffaireService.changePassword(userId, changePasswordRequest);

        // Then
        assertTrue(new BCryptPasswordEncoder().matches(newPassword, user.getPwd())); // Verify new password is correctly encoded and set
    }


    @Test
    public void should_throw_wrong_password_exception_when_old_password_does_not_match() {
        // Given
        String userId = "user123";
        String oldPassword = "incorrectOldPassword"; // Provide incorrect old password
        String newPassword = "newPassword";

        User user = new User();
        user.setId(userId);
        user.setPwd(new BCryptPasswordEncoder().encode("oldPassword")); // Encrypt old password

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword(newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(WrongPasswordException.class, () -> domiAffaireService.changePassword(userId, changePasswordRequest));
    }

}