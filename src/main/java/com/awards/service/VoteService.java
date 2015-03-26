package com.awards.service;

import com.awards.model.Vote;
import com.awards.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public void castVote(Vote vote){
        Vote existingVote = voteRepository.findExistingVote(vote.getUserId(), vote.getCategoryId());
        if(existingVote == null){
            log.info("Casting a new vote {}", vote);
            voteRepository.save(vote);
        }else{
            log.info("Updating vote {}, new vote is {}", existingVote, vote);
            voteRepository.delete(existingVote);
            voteRepository.save(vote);
        }
    }
}
