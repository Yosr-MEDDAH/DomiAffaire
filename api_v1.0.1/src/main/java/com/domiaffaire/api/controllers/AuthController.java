package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.PasswordUrlVerification;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.repositories.VerificationTokenRepository;
import com.domiaffaire.api.services.AuthService;
import com.domiaffaire.api.services.jwt.UserService;
import com.domiaffaire.api.entities.VerificationToken;
import com.domiaffaire.api.utils.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;


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
    private final Mapper mapper;
    private final RegistrationCompleteEventListener eventListener;
    @PostMapping("/signup")
    public ResponseEntity<?> createCustomer(@RequestBody @Valid SignupRequest signupRequest, final HttpServletRequest request){
        if(authService.hasUserWithEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email Already exists");
        UserDTO createdUserDto = authService.createUser(signupRequest);
        User user = new User();
        BeanUtils.copyProperties(createdUserDto,user);
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
        if(user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request !");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
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
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is not enabled");
                authenticationResponse.setJwt(jwt);
                authenticationResponse.setUserId(optionalUser.get().getId());
                authenticationResponse.setUserRole(optionalUser.get().getUserRole());
                return ResponseEntity.ok(authenticationResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
    @PostMapping("/password-reset-request")
    public PasswordUrlVerification resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest,final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userRepository.findFirstByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        if(user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(),passwordResetToken);
            passwordResetUrl= passwordResetEmailLink(user.get(),applicationUrl(request),passwordResetToken);
        }
        PasswordUrlVerification passwordUrlVerification = new PasswordUrlVerification();
        passwordUrlVerification.setId(1);
        passwordUrlVerification.setUrl(passwordResetUrl);
        return passwordUrlVerification;
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String passwordResetToken){
        String tokenValidationResult = userService.validatePasswordResetToken(passwordResetToken);
        if(!tokenValidationResult.equalsIgnoreCase("valid")){
            return "Invalid Password reset token";
        }
        User user = userService.findUserByPasswordToken(passwordResetToken);
        if(user != null && passwordResetRequest.getNewPassword().equalsIgnoreCase(passwordResetRequest.getConfirmPassword())){
            userService.resetUserPassword(user, passwordResetRequest.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/api/auth/reset-password?token="+passwordResetToken;
        eventListener.sendPasswordResetVerificationEmail();
        log.info("Click the link to reset your password: {}",url);
        return url;
    }


}

