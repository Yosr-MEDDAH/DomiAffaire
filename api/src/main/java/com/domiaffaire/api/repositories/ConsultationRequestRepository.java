package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.ConsultationRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.ConsultationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRequestRepository extends MongoRepository<ConsultationRequest,String> {
    List<ConsultationRequest> findAllBySentByOrderBySentAtDesc(User user);
    List<ConsultationRequest> findAllByStatusIsOrderBySentAtDesc(ConsultationStatus status);
    List<ConsultationRequest> findAllByStatusIsOrStatusIsOrderBySentAtDesc(ConsultationStatus status1, ConsultationStatus status2);
}
