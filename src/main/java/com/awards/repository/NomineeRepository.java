package com.awards.repository;

import com.awards.model.Nominee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NomineeRepository extends MongoRepository<Nominee, String> {
    @Query("{ 'categoryId' : ?0 }")
    List<Nominee> findNomineesByCategoryId(String categoryId);
    @Query("{ 'name': { $in: ?0 } }")
    List<Nominee> findNomineesByName(List<String> nomineesName);
    @Query("{ 'name' : ?0 }")
    Nominee findNomineeByName(String name);
}
