package com.example.backend.mapper.user;

import com.example.backend.dto.model.UserDto;
import com.example.backend.entity.UserEntity;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNo()
        );
    }
}
