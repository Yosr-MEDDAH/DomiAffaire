package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Reservation;
import com.domiaffaire.api.enums.ReservationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation,String> {
    List<Reservation> findAllByOrderByCreatedAtDesc();
    List<Reservation> findAllByStatusIsOrderByCreatedAtDesc(ReservationStatus status);
    List<Reservation> findAllByStatusIs(ReservationStatus status);
}
