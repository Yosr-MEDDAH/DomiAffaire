package com.domiaffaire.api.repositories;


import com.domiaffaire.api.entities.Faq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FaqRepository extends MongoRepository<Faq,String> {
}
