package com.domiaffaire.api.dto;

import com.domiaffaire.api.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private UserRole userRole;
    private String userId;
}
