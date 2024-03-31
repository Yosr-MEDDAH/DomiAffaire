package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Client extends User{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthDate;

}
