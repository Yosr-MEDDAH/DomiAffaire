package com.domiaffaire.api.dto;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountantDTO extends UserDTO{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String code;
}
