package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Deadline;
import com.domiaffaire.api.entities.DomiciliationRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.DomiciliationRequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomiciliationRequestRepository extends MongoRepository<DomiciliationRequest,String> {
    List<DomiciliationRequest> findAllByStatusIsOrderByCreatedAtDesc(DomiciliationRequestStatus status);
    List<DomiciliationRequest> findAllByClientAndDeadlineIsNullOrderByCreatedAtDesc(User user);
    List<DomiciliationRequest> findAllByDeadlineNotNull();
    DomiciliationRequest findByDeadline(Deadline deadline);
    List<DomiciliationRequest> findAllByClient(User user);
    List<DomiciliationRequest> findAllByDocumentCodeIsNotNull();
    List<DomiciliationRequest> findAllByShareCapitalIsNotNull();
}
