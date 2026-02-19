package com.example.backend.mapper.url;

import com.example.backend.dto.model.UrlDto;
import com.example.backend.entity.UrlEntity;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {
    private UrlMapper() {
    }

    public static UrlDto toDto(UrlEntity urlEntity) {
        String shortUrl =
                "/r/" + urlEntity.getSlug() + "-" + urlEntity.getShortId();

        return new UrlDto(
                urlEntity.getShortId(),
                urlEntity.getSlug(),
                urlEntity.getOriginalUrl(),
                shortUrl,
                urlEntity.getCreatedAt()
        );
    }
}
