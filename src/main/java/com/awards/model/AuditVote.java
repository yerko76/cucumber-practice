package com.awards.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
public class AuditVote {
    @Id
    private String id;

}
