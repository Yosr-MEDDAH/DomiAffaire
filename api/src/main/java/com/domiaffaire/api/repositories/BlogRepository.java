package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Blog;
import com.domiaffaire.api.entities.BlogCategory;
import com.domiaffaire.api.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
    List<Blog> findAllByCategoryIsAndIsArchivedFalse(BlogCategory blogCategory);
    List<Blog> findAllByCreatedByAndIsArchivedFalseOrderByCreatedAtDesc(User user);
    List<Blog> findAllByIsArchivedFalseOrderByCreatedAtDesc();
    List<Blog> findAllByOrderByCreatedAtDesc();

}
