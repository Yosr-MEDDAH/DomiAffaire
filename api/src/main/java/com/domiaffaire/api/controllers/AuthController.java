package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.PasswordUrlVerification;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.exceptions.WrongCodeAccountantException;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.repositories.VerificationTokenRepository;
import com.domiaffaire.api.services.AuthService;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import com.domiaffaire.api.services.jwt.UserService;
import com.domiaffaire.api.entities.VerificationToken;
import com.domiaffaire.api.utils.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;
    private Set<String> tokenBlacklist = new HashSet<>();
    private final DomiAffaireServiceImpl service;

    @GetMapping("/{email}")
    public ResponseEntity<?> findClientByEmail(@PathVariable String email){
        try {
            User client = service.findUserByEmail(email);
            return ResponseEntity.ok(client);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }


    @PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCustomer(@RequestPart("image") MultipartFile file,
                                            @RequestPart("signupRequest") @Valid SignupRequest signupRequest,
                                            final HttpServletRequest request) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("{\"message\":\"Email already exists\"}");
        }

        try {
            byte[] imageBytes = file.getBytes();
            User createdUser = authService.createUser(signupRequest, imageBytes);
            if (createdUser != null) {
                publisher.publishEvent(new RegistrationCompleteEvent(createdUser, applicationUrl(request)));
                try {
                    eventListener.sendVerificationEmail();
                } catch (MessagingException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Email not valid\"}");
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Check your mail to verify your account\"}");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Failed to create user\"}");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Failed to process image file.\"}");
        } catch (WrongCodeAccountantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Code accountant does not match.\"}");
        }
    }




    @GetMapping("/verifyEmail")
    public RedirectView verifyEmail(@RequestParam("token") String token){
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken.getUser().isEnabled()){
            return new RedirectView("http://localhost:4200/invalid-token");
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            return new RedirectView("http://localhost:4200/login");
        }
        return new RedirectView("http://localhost:4200/invalid-token");
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPwd()));
            final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
            Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            if (optionalUser.isPresent()) {
                if(!optionalUser.get().isEnabled())
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Account is not enabled\"}");
                authenticationResponse.setJwt(jwt);
                authenticationResponse.setUserId(optionalUser.get().getId());
                authenticationResponse.setUserRole(optionalUser.get().getUserRole());
                return ResponseEntity.ok(authenticationResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Invalid username or password.\"}");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Invalid username or password.\"}");
        }
    }



    @PostMapping("/password-reset-request")
    public ResponseEntity<?> resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest,final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userRepository.findFirstByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        if(user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(),passwordResetToken);
            publisher.publishEvent(new RegistrationCompleteEvent(user.get(), applicationUrl(request)));
            passwordResetUrl= passwordResetEmailLink(user.get(),applicationUrl(request),passwordResetToken);
            PasswordUrlVerification passwordUrlVerification = new PasswordUrlVerification();
            passwordUrlVerification.setId(1);
            passwordUrlVerification.setUrl(passwordResetUrl);
            return ResponseEntity.ok(passwordUrlVerification);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User does not exist with this email : " + passwordResetRequest.getEmail() + "\"}");
        }

    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPassword resetPassword,
                                           @RequestParam("token") String passwordResetToken){
        String tokenValidationResult = userService.validatePasswordResetToken(passwordResetToken);
        if(!tokenValidationResult.equalsIgnoreCase("valid")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid password reset token\"}");
        }
        User user = userService.findUserByPasswordToken(passwordResetToken);
        if(user != null && resetPassword.getNewPassword().equalsIgnoreCase(resetPassword.getConfirmPassword())){
            userService.resetUserPassword(user, resetPassword.getNewPassword());
            return ResponseEntity.ok().body("{\"message\": \"Password has been reset successfully\"}");

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid password reset token\"}");
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/api/auth/reset-password?token="+passwordResetToken;
        eventListener.sendPasswordResetVerificationEmail(user);
        log.info("Click the link to reset your password: {}",url);
        return url;
    }


    @GetMapping("/clients")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }








}