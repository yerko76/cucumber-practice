package com.awards.repository;


import com.awards.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface VoteRepository extends MongoRepository<Vote, String > {
    @Query( value = "{ $and: [ { 'categoryId' : ?0 }, { 'nomineeId' : ?1} ]}", count = true)
    int getTotalVotesByCategoryAndNominee(String categoryId, String nomineeId);

    @Query("{ $and: [ { 'userId' : ?0 }, { 'categoryId' : ?1} ]}")
    Vote findExistingVote(String userId, String categoryId);
}