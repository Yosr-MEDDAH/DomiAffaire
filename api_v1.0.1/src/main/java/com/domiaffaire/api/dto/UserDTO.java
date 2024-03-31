package com.domiaffaire.api.dto;

import com.domiaffaire.api.enums.UserRole;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private String pwd;
    private String image;
    private UserRole userRole;
}
