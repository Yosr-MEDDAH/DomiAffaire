package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.SignupRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
import com.domiaffaire.api.exceptions.WrongCodeAccountantException;
import com.domiaffaire.api.repositories.PasswordResetTokenRepository;
import com.domiaffaire.api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClientUser() throws WrongCodeAccountantException {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(UserRole.CLIENT.toString());
        signupRequest.setEmail("client@example.com");
        signupRequest.setFirstName("Client");
        signupRequest.setLastName("User");
        signupRequest.setPwd("password");
        signupRequest.setPhoneNumber("1234567890");
        signupRequest.setBirthDate(new Date());

        byte[] imageBytes = new byte[0];

        User savedUser = new User();
        savedUser.setId("user1");
        savedUser.setEmail("client@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User createdUser = authService.createUser(signupRequest, imageBytes);

        assertNotNull(createdUser);
        assertEquals("client@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateAccountantUserWithCorrectCode() throws WrongCodeAccountantException {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(UserRole.ACCOUNTANT.toString());
        signupRequest.setEmail("accountant@example.com");
        signupRequest.setFirstName("Accountant");
        signupRequest.setLastName("User");
        signupRequest.setPwd("password");
        signupRequest.setPhoneNumber("1234567890");
        signupRequest.setCode("abcd");

        byte[] imageBytes = new byte[0];

        User savedUser = new User();
        savedUser.setId("user2");
        savedUser.setEmail("accountant@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User createdUser = authService.createUser(signupRequest, imageBytes);

        assertNotNull(createdUser);
        assertEquals("accountant@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateAccountantUserWithWrongCode() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(UserRole.ACCOUNTANT.toString());
        signupRequest.setEmail("accountant@example.com");
        signupRequest.setFirstName("Accountant");
        signupRequest.setLastName("User");
        signupRequest.setPwd("password");
        signupRequest.setPhoneNumber("1234567890");
        signupRequest.setCode("wrongcode");

        byte[] imageBytes = new byte[0];

        assertThrows(WrongCodeAccountantException.class, () -> {
            authService.createUser(signupRequest, imageBytes);
        });

        verify(userRepository, times(0)).save(any(User.class));
    }
}