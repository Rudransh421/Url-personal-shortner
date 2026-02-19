package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUrlRequest(

        @NotBlank
        @Size(max = 100)
        String slug,

        @NotBlank
        @Pattern(
                regexp = "^(https?|ftp)://.*$",
                message = "Invalid URL format"
        )
        String originalUrl
) {
}
