package com.example.backend.controller;


import com.example.backend.exception.ApiException;
import com.example.backend.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class RedirectController {

    private final UrlService urlService;

    @GetMapping("/r/{slug}-{shortId:[a-zA-Z0-9]{7}}")
    public ResponseEntity<Void> redirect(
            @PathVariable String slug,
            @PathVariable String shortId
    ) {

        try {
            String target = urlService.resolveRedirect(slug, shortId);

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(target))
                    .build();

        } catch (ApiException ex) {

            if (ex.getStatus() == HttpStatus.NOT_FOUND) {
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .location(URI.create("http://localhost:5173/not-found"))
                        .build();
            }

            throw ex;
        }
    }

}

