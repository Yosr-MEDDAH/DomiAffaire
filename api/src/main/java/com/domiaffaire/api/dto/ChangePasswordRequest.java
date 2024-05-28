package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull(message="password shouldn't be null")
    @NotBlank(message="password shouldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "Password is weak")
    private String oldPassword;
    @NotNull(message="password shouldn't be null")
    @NotBlank(message="password shouldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "Password is weak")
    private String newPassword;
    @NotNull(message="password shouldn't be null")
    @NotBlank(message="password shouldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "Password is weak")
    private String confirmPassword;
}
