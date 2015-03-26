package com.awards.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Vote")
@NoArgsConstructor
@ToString
@Getter
public class Vote {
    @Id
    private String id;
    private String userId;
    private String categoryId;
    private String nomineeId;

    public Vote(String userId,
                String categoryId,
                String nomineeId){
        this.userId = userId;
        this.categoryId = categoryId;
        this.nomineeId = nomineeId;
    }

}
