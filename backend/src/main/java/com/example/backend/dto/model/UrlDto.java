package com.example.backend.dto.model;

import java.time.Instant;

public record UrlDto(
        String shortId,
        String slug,
        String originalUrl,
        String shortUrl,
        Instant createdAt
) {
}
