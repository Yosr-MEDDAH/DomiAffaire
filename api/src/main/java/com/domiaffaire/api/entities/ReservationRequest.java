package com.domiaffaire.api.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "reservation_requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class ReservationRequest {
    @Id
    private String id;
    private int nbrPlaces;
    private List<String> requiredEquipments = new ArrayList<>();
    private int duration;
    private Date startDate;
    private int numDays;
    private int hourBeginingSuggested;
    private int hourEndingSuggested;
    private String nature;
    private String title;
    private LocalDateTime createdAt = LocalDateTime.now();
    @DBRef
    private User sentBy;

}
