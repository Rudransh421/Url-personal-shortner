package com.example.backend.security;

import com.example.backend.entity.UserEntity;
import com.example.backend.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;


    private String buildToken(UserEntity user, String secret, long expiration) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("name", user.getName())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(UserEntity user) {
        return buildToken(user, refreshSecret, refreshExpiration);
    }

    public String generateAccessToken(UserEntity user) {
        return buildToken(user, accessSecret, accessExpiration);
    }

    public String extractUsernameFromAccessToken(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(accessSecret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid access token");
        }
    }

    public String extractUsernameFromRefreshToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(refreshSecret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    private Key getSignInKey(String secret) {

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean isAccessTokenValid(String accessToken, UserEntity userEntity) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(accessSecret))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            boolean notExpired = expiration.after(new Date());
            boolean matchesEmail = username.equals(userEntity.getEmail());
            boolean matchesPhone = username.equals(userEntity.getPhoneNo());

            boolean correctUser = matchesEmail || matchesPhone;


            return notExpired && correctUser;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
