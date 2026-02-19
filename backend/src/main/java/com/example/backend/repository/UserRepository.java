package com.example.backend.repository;

import com.example.backend.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailOrPhoneNo(String email, String phoneNo);

    Optional<UserEntity> findByRefreshToken(String refreshToken);
}
