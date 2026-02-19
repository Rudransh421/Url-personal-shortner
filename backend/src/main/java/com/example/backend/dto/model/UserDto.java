package com.example.backend.dto.model;

public record UserDto(
        String id,
        String name,
        String email,
        String phoneNo
) {
}