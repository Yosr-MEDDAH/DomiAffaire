package com.domiaffaire.api.dto;

import com.domiaffaire.api.enums.UserRole;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private String id;
    private String email;
    private String pwd;
    private byte[] image;
    private UserRole userRole;
    private int domiciliationNotificationCount;
}
