package com.domiaffaire.api.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String pwd;
}
