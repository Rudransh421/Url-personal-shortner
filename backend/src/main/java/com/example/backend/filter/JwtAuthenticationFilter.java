package com.example.backend.filter;

import com.example.backend.entity.UserEntity;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = extractAccessTokenFromCookies(request);

        if (accessToken != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                String username =
                        jwtService.extractUsernameFromAccessToken(accessToken);

                userRepository.findByEmailOrPhoneNo(username, username)
                        .ifPresent(userEntity -> {

                            if (jwtService.isAccessTokenValid(accessToken, userEntity)) {

                                UsernamePasswordAuthenticationToken authToken = getUsernamePasswordAuthenticationToken(userEntity);

                                authToken.setDetails(
                                        new WebAuthenticationDetailsSource()
                                                .buildDetails(request)
                                );

                                SecurityContextHolder.getContext()
                                        .setAuthentication(authToken);
                            }
                        });

            } catch (Exception ignored) {
                // If token invalid or expired → do nothing
                // Spring will handle 401 on protected routes
            }
        }

        filterChain.doFilter(request, response);
    }

    @Nonnull
    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UserEntity userEntity) {
        User springUser = new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                Collections.emptyList()
        );

        return new UsernamePasswordAuthenticationToken(
                springUser,
                null,
                springUser.getAuthorities()
        );
    }

    private String extractAccessTokenFromCookies(HttpServletRequest request) {

        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
