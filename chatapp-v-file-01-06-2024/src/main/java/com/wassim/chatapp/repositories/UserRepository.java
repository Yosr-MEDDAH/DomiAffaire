package com.wassim.chatapp.repositories;

import com.wassim.chatapp.entities.User;
import com.wassim.chatapp.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findFirstByEmail(String email);
    List<User> findAllByUserRole(UserRole role);
}
