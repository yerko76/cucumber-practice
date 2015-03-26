package com.awards.controller;


import com.awards.model.Nominee;
import com.awards.repository.NomineeRepository;
import com.wordnik.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(value = "Nominee", description = "Search, create and delete nominees")
public class NomineeController {

    @Autowired
    private NomineeRepository nomineeRepository;

    @ApiOperation(value = "saves a list of nominees by category")
    @ApiResponses( {@ApiResponse( code = 201, message = "Should always return Http Status CREATED" )} )
    @RequestMapping(value = "/nominees/create", method = RequestMethod.POST)
    public ResponseEntity<?> saveCategories(@ApiParam(name = "List of nominees", value = "List of nominees to be saved", required = true)
                                            @RequestBody(required = true) List<Nominee> nominees) {
        log.info("Saving category {}", nominees.toString());
        nomineeRepository.save(nominees);
        return new ResponseEntity<String>("Nominees created", HttpStatus.CREATED);
    }

    @ApiOperation(value = "return all nominees for specific category-id")
    @ApiResponses( {@ApiResponse( code = 200, message = "Should always return Http Status OK" )} )
    @RequestMapping(value = "/nominees/search", method = RequestMethod.GET)
    public ResponseEntity<?> getNomineesByCategory(@ApiParam(name="category-id", value="The Id of the category-id", required=true)
                                                   @RequestParam("category-id") String categoryId) {
        return new ResponseEntity<List<Nominee>>(nomineeRepository.findNomineesByCategoryId(categoryId), HttpStatus.OK);

    }
    @RequestMapping(value = "/nominees/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteNominees(@RequestBody(required = true) List<Nominee> nominees){
        log.info("deleting the following nominees", nominees.toString());
        nomineeRepository.delete(nominees);
        return new ResponseEntity<String>("Nominees deleted", HttpStatus.OK);
    }


}
