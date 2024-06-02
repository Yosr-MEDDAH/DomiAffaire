package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.ReservationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRequestRepository extends MongoRepository<ReservationRequest,String> {
    List<ReservationRequest> findAllByOrderByCreatedAtDesc();
}
