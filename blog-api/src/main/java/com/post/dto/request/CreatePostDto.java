package com.post.dto;

public record CreatePostDto(
        String title,
        String content,
        Long idUser) {
}
