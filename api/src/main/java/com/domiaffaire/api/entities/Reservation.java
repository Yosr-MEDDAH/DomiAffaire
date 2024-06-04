package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.ReservationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Reservation {
    @Id
    private String id;
    private LocalDateTime dateBegining;
    private LocalDateTime dateEnding;
    private LocalDateTime createdAt= LocalDateTime.now();
    @DBRef
    private Room room;
    private ReservationStatus status = ReservationStatus.IN_PROGRESS;
    @DBRef
    private User client;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation other = (Reservation) o;

        return Objects.equals(dateBegining, other.dateBegining) &&
                Objects.equals(dateEnding, other.dateEnding) &&
                Objects.equals(room, other.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateBegining, dateEnding, room);
    }
}
