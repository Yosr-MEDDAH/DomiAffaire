package com.domiaffaire.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "packs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Pack {
    @Id
    private String id;
    private String designation;
    private float price;
    private String description;
    @JsonIgnore
    private Set<User> users = new HashSet<>();
    private int userCount = 0;
}
