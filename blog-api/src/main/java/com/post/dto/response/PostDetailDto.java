package com.post.dto.response;

import java.time.Instant;

public record PostDetailDto(
        Long idPost,
        String title,
        String content,
        Instant fecha_creacion,
        Long idUser) {
}
