package com.comment.dto.response;

import java.time.Instant;

public record DetailCommentDto(
        String content,
        Long idUser,
        Long idPost,
        Instant fechaCreacion) {
}
