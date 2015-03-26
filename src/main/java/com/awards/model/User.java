package com.awards.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String userName;
    @JsonIgnore
    private String userPassword;
}
