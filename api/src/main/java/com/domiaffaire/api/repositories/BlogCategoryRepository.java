package com.domiaffaire.api.repositories;


import com.domiaffaire.api.entities.BlogCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BlogCategoryRepository extends MongoRepository<BlogCategory,String> {
    Optional<BlogCategory> findByName(String name);
    List<BlogCategory> findAllByOrderByCreatedAtDesc();
}
