package com.back.domain.post.postComment.dto;

import com.back.domain.post.postComment.entity.PostComment;

import java.time.LocalDateTime;

public record PostCommentDto(
        int id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String content
) {
    public PostCommentDto(PostComment comment) {
        this(
                comment.getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getContent()
        );
    }
}