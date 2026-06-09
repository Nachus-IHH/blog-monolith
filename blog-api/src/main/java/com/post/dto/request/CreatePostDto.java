package com.post.dto.request;

public record CreatePostDto(
        String title,
        String content,
        Long idUser) {
}
