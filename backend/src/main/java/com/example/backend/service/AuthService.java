package com.example.backend.service;

import com.example.backend.dto.model.UserDto;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.dto.response.GetUserResponse;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.user.UserMapper;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {

        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");
        }

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        try {

            UserEntity user = UserEntity.builder()
                    .name(req.getName())
                    .email(req.getEmail())
                    .phoneNo(req.getPhoneNo())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .build();

            userRepository.save(user);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            UserDto userDto = UserMapper.toDto(user);

            return new AuthResponse(accessToken, refreshToken, userDto);

        } catch (ApiException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to register user at this time"
            );
        }
    }


    public AuthResponse login(LoginRequest req) {

        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        try {

            UserEntity user = userRepository.findByEmailOrPhoneNo(req.getUsername(), req.getUsername()).orElse(null);

            if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            UserDto userDto = UserMapper.toDto(user);

            return new AuthResponse(accessToken, refreshToken, userDto);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to login at this time"
            );
        }
    }

    public void logout(String refreshToken) {

        if (refreshToken == null) {
            return;
        }

        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    user.setRefreshToken(null);
                    userRepository.save(user);
                });
    }

    public GetUserResponse getUserByUsername(String username) {

        UserEntity userEntity =
                userRepository.findByEmailOrPhoneNo(username, username)
                        .orElseThrow(() ->
                                new ApiException(HttpStatus.UNAUTHORIZED, "User not found")
                        );

        UserDto userDto = UserMapper.toDto(userEntity);

        return new GetUserResponse(userDto);
    }

    public AuthResponse refreshAccessToken(String refreshToken) {

        if (refreshToken == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        String username = jwtService.extractUsernameFromRefreshToken(refreshToken);

        UserEntity userEntity = userRepository.findByEmailOrPhoneNo(username, username).orElse(null);

        if (userEntity == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        if (!refreshToken.equals(userEntity.getRefreshToken())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(userEntity);
        String newRefreshToken = jwtService.generateRefreshToken(userEntity);

        userEntity.setRefreshToken(newRefreshToken);
        userRepository.save(userEntity);
        UserDto userDto = UserMapper.toDto(userEntity);


        return new AuthResponse(newAccessToken, newRefreshToken, userDto);

    }
}
