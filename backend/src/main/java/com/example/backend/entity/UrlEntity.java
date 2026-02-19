package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "urls")
@CompoundIndex(
        name = "user_slug_unique_idx",
        def = "{'userId':1,'slug':1}",
        unique = true
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UrlEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String shortId;

    private String slug;

    private String originalUrl;

    @Indexed
    private String userId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
