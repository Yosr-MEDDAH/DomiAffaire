package com.wassim.chatapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class User {
    @Id
    private String id;
    private String email;
    private String pwd;
    private byte[] image;
    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthDate;
    private String code;
    private UserRole userRole;
    private boolean isEnabled = false;
    private boolean isNotArchived = true;

    @DBRef
    @JsonIgnore
    private List<Chat> userChats = new ArrayList<>();




}
