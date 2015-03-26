package com.awards.repository;

import com.awards.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query("{ 'id' : { $in: ?0 } }")
    List<Category> findByIds(List<String> ids);

    @Query("{ 'name' : ?0 }")
    Category findByName(String categoryName);
}
