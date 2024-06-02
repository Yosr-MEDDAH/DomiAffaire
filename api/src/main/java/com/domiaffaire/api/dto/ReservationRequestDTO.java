package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReservationRequestDTO {
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
    private LocalDateTime createdAt;
    private User sentBy;
}
