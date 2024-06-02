package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Deadline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeadlineRepository extends MongoRepository<Deadline,String> {
    List<Deadline> findAllByLimitedDateBetween(LocalDateTime date1, LocalDateTime date2);

}
