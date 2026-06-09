package com.comment.dto.request;

public record CreateCommentDto(
        String content,
        Long idUser,
        Long idPost) {
}
