package com.domiaffaire.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateAdminProfileRequest {
    @NotNull(message="username shouldn't be null")
    @NotBlank(message="username shouldn't be blank")
    private String username;
}
