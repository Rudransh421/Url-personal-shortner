package com.example.backend.controller;

import com.example.backend.dto.request.CreateUrlRequest;
import com.example.backend.dto.response.MyUrlResponse;
import com.example.backend.dto.response.UrlResponse;
import com.example.backend.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponse> createUrl(
            @Valid @RequestBody CreateUrlRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        UrlResponse response =
                urlService.createUrl(request, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<MyUrlResponse> getMyUrls(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        MyUrlResponse urls =
                urlService.getUserUrls(userId);

        return ResponseEntity.ok(urls);
    }

}
