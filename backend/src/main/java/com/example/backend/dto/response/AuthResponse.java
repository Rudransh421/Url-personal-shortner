package com.example.backend.dto.response;

import com.example.backend.dto.model.UserDto;

// used for login and signup
public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserDto user

) {}
