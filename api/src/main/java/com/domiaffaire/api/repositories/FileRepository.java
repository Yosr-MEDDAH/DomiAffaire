package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File,String> {
    Optional<File> findByName(String fileName);
    List<File> findAllByCompanyCreationIsTrue();
}
