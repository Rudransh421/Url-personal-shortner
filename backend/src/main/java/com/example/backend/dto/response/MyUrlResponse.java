package com.example.backend.dto.response;

import java.util.List;

public record MyUrlResponse(
        List<UrlResponse> urls
) {
}
