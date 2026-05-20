package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class ApiV1PostCommentController {
    private final PostService postService;

    @GetMapping
    public List<PostCommentDto> getItems(@PathVariable int postId) {
        Optional<Post> optionalPost = postService.findById(postId);
        if (optionalPost.isEmpty()) return null;
        Post post = optionalPost.get();

        List<PostComment> comments = post.getComments();
        return comments.stream().map(PostCommentDto::new).toList();
    }

    @GetMapping("/{id}")
    public PostCommentDto getItem(@PathVariable int postId, @PathVariable int id) {
        Optional<Post> optionalPost = postService.findById(postId);
        if (optionalPost.isEmpty()) return null;
        Post post = optionalPost.get();

        Optional<PostComment> optionalComment = post.findCommentById(id);
        if (optionalComment.isEmpty()) return null;
        PostComment comment = optionalComment.get();

        return new PostCommentDto(comment);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deleteItem(@PathVariable int postId, @PathVariable int id) {
        Optional<Post> optionalPost = postService.findById(postId);
        if (optionalPost.isEmpty()) return null;
        Post post = optionalPost.get();

        Optional<PostComment> optionalComment = post.findCommentById(id);
        if (optionalComment.isEmpty()) return null;
        PostComment comment = optionalComment.get();

        if (!postService.deleteComment(post, comment)) return null;

        return new RsData<>(
                "200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(comment.getId())
        );
    }
}