package com.awards.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Nominee")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Nominee {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String videoUrl;
    private String categoryId;

    public Nominee(String name, String description, String imageUrl, String videoUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
