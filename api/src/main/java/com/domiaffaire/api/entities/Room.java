package com.domiaffaire.api.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Room {
    @Id
    private String id;
    private String name;
    private int nbrPlaces;
    private LocalDateTime createdAt = LocalDateTime.now();
    private List<String> equipments = new ArrayList<>();
}
