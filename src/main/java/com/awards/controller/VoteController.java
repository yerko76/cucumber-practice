package com.awards.controller;

import com.awards.model.Vote;
import com.awards.service.VoteService;
import com.wordnik.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(value = "Votes", description = "Cast, and updates votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @ApiOperation(value = "Cast one vote")
    @ApiResponses( {@ApiResponse( code = 200, message = "Should always return Http Status OK" )} )
    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public ResponseEntity<?> castVote(@ApiParam(name = "Vote", value = "Vote to be casted", required = true)
                                            @RequestBody(required = true) Vote vote) {
        log.info("Casting one vote for user {}, category {}, nominee {}", vote.getUserId()
                ,vote.getCategoryId()
                , vote.getNomineeId());
        voteService.castVote(vote);
        return new ResponseEntity<String>("Vote casted", HttpStatus.OK);
    }
}
