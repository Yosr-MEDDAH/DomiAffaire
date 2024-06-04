package com.domiaffaire.api.dto;

import com.domiaffaire.api.entities.Room;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.ReservationStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private String id;
    private LocalDateTime dateBegining;
    private LocalDateTime dateEnding;
    private LocalDateTime createdAt;
    @DBRef
    private Room room;
    private ReservationStatus status;
    @DBRef
    private User client;
}
