package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Chat;
import com.domiaffaire.api.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message,String> {
}
