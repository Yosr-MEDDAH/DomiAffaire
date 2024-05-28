package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Chat;
import com.domiaffaire.api.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository  extends MongoRepository<Chat,String> {
    List<Chat> findAllByAccountantIsOrClientIs(User user1, User user2);
    List<Chat> findAllByClientIs(User user);
    List<Chat> findAllByAccountantIs(User user);
}
