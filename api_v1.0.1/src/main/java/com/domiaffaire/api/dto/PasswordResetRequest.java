package com.domiaffaire.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @Email(message="invalid email")
    private String email;
    @NotNull(message="password shouldn't be null")
    @NotBlank(message="password shouldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    private String newPassword;
    @NotNull(message="password shouldn't be null")
    @NotBlank(message="password shouldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    private String confirmPassword;
}
