package com.post.dto.response;

import java.util.Date;

public record PostDetailDto(
        Long idPost,
        String title,
        String content,
        Date fecha_creacion,
        Long idUser) {
}
