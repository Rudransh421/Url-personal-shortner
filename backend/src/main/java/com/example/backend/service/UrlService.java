package com.example.backend.service;

import com.example.backend.dto.model.UrlDto;
import com.example.backend.dto.request.CreateUrlRequest;
import com.example.backend.dto.response.MyUrlResponse;
import com.example.backend.dto.response.UrlResponse;
import com.example.backend.entity.UrlEntity;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.url.UrlMapper;
import com.example.backend.repository.UrlRepository;
import com.example.backend.utility.NanoIdGenerator;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final NanoIdGenerator nanoIdGenerator;


    public UrlResponse createUrl(CreateUrlRequest request, String userId) {

        urlRepository.findByUserIdAndSlug(userId, request.slug())
                .ifPresent(existing -> {
                    throw new ApiException(HttpStatus.CONFLICT, "Slug already exists");
                });

        String shortId = nanoIdGenerator.generate();

        UrlEntity urlEntity = UrlEntity.builder().shortId(shortId)
                .slug(request.slug())
                .originalUrl(request.originalUrl())
                .userId(userId)
                .createdAt(Instant.now())
                .build();

        try {
            urlRepository.save(urlEntity);
        } catch (DuplicateKeyException e) {

            if (e.getMessage().contains("user_slug_unique_idx")) {
                throw new ApiException(HttpStatus.CONFLICT, "Slug already exists");
            }

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to create short URL"
            );
        }

        UrlDto url = UrlMapper.toDto(urlEntity);

        return new UrlResponse(url);
    }

    public MyUrlResponse getUserUrls(String userId) {

        List<UrlResponse> urls = urlRepository.findByUserId(userId).stream()
                .map(UrlMapper::toDto)
                .map(UrlResponse::new)
                .toList();

        return new MyUrlResponse(urls);
    }

    public String resolveRedirect(String slug, String shortId) {

        UrlEntity entity = urlRepository.findByShortId(shortId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Url not found"));

        if (!entity.getSlug().equals(slug)) {
            return "/r/" + entity.getSlug() + "-" + shortId;
        }

        return entity.getOriginalUrl();
    }
}
