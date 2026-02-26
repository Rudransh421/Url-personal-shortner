package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.dto.response.GetUserResponse;
import com.example.backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.cookie.secure}")
    private boolean secureCookie;
    private final AuthService authService;

    private static final String accessToken = "accessToken";
    private static final String refreshToken = "refreshToken";
    private static final String setCookie = "Set-Cookie";

    private static final long refreshMaxAge = 7 * 60 * 60 * 24;
    private static final long accessMaxAge = 15 * 60;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req, HttpServletResponse response) {

        AuthResponse authResponse = authService.register(req);

        ResponseCookie accessCookie = ResponseCookie.from(accessToken, authResponse.accessToken())
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(refreshToken, authResponse.refreshToken())
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(refreshMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(setCookie, accessCookie.toString());
        response.addHeader(setCookie, refreshCookie.toString());

        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req, HttpServletResponse response) {

        AuthResponse authResponse = authService.login(req);

        ResponseCookie accessCookie = ResponseCookie.from(accessToken, authResponse.accessToken())
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(refreshToken, authResponse.refreshToken())
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(refreshMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(setCookie, accessCookie.toString());
        response.addHeader(setCookie, refreshCookie.toString());

        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {

        authService.logout(refreshToken);

        ResponseCookie accessDelete = ResponseCookie.from(accessToken, "")
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshDelete = ResponseCookie.from(refreshToken, "")
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(setCookie, accessDelete.toString());
        response.addHeader(setCookie, refreshDelete.toString());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-user")
    public ResponseEntity<GetUserResponse> getUser(Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(authService.getUserByUsername(username));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {

        AuthResponse res = authService.refreshAccessToken(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from(accessToken, res.accessToken())
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(refreshToken, res.refreshToken())
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(refreshMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(setCookie, accessCookie.toString());
        response.addHeader(setCookie, refreshCookie.toString());

        return ResponseEntity.ok(res);
    }
}
