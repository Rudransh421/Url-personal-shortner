package com.example.backend.repository;

import com.example.backend.entity.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {

    Optional<UrlEntity> findByShortId(String shortId);

    List<UrlEntity> findByUserId(String userId);

    Optional<UrlEntity> findByUserIdAndSlug(String userId, String slug);
}
