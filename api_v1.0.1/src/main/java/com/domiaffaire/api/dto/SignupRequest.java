package com.domiaffaire.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignupRequest {
    @Email(message="invalid email")
    private String email;
    @NotNull(message="password shouldn't be null")
    @NotBlank(message="password shouldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    private String pwd;
    private String image;
    @NotNull(message="firstname shouldn't be null")
    @NotBlank(message="firstname shouldn't be blank")
    private String firstName;
    @NotNull(message="lastname shouldn't be null")
    @NotBlank(message="lastname shouldn't be blank")
    private String lastName;
    @NotNull(message = "phone number shouldn't be blank")
    @NotBlank(message="phone number shouldn't be blank")
    @Pattern(regexp = "^[2-5|9]\\d{7}$", message = "Invalid phone number")
    private String phoneNumber;
    @NotNull(message="birthdate shouldn't be null")
    private Date birthDate;

}
