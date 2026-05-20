package com.back.domain.post.post.dto;

import com.back.domain.post.post.entity.Post;

import java.time.LocalDateTime;

public record PostDto(
        int id,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        String subject,
        String body
) {
    public PostDto(Post post) {
        this(
                post.getId(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getTitle(),
                post.getContent()
        );
    }
}