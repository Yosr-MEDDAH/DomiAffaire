package com.wassim.chatapp.repositories;

import com.wassim.chatapp.entities.Chat;
import com.wassim.chatapp.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat,String> {
    List<Chat> findAllByAccountantIsOrClientIs(User user1, User user2);
}
