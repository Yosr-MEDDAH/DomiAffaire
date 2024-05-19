package com.domiaffaire.api.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientDTO extends UserDTO{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthDate;
}
