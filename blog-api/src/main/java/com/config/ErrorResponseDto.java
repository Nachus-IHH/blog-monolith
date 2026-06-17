package com.config;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        int statusCode,
        LocalDateTime timestamp) {
}
