package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "ApiV1PostCommentController", description = "API 댓글 컨트롤러")
public class ApiV1PostCommentController {
    private final PostService postService;

    public record PostCommentCreateReqBody(
            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {
    }

    @PostMapping
    @Transactional
    @Operation(summary = "작성")
    public RsData<PostCommentDto> create(
            @PathVariable int postId,
            @RequestBody @Valid PostCommentCreateReqBody reqBody
    ) {
        Post post = postService.findById(postId).orElseThrow();

        PostComment comment = postService.writeComment(post, reqBody.content);

        postService.flush();

        return new RsData<>(
                "201-1",
                "%d번 댓글이 생성되었습니다.".formatted(comment.getId()),
                new PostCommentDto(comment)
        );
    }

    @GetMapping
    @Operation(summary = "다건 조회")
    public List<PostCommentDto> list(@PathVariable int postId) {
        Post post = postService.findById(postId).orElseThrow();

        List<PostComment> comments = post.getComments();

        return comments.stream().map(PostCommentDto::new).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    public PostCommentDto read(@PathVariable int postId, @PathVariable int id) {
        Post post = postService.findById(postId).orElseThrow();

        PostComment comment = post.findCommentById(id).orElseThrow();

        return new PostCommentDto(comment);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    public RsData<Void> delete(@PathVariable int postId, @PathVariable int id) {
        Post post = postService.findById(postId).orElseThrow();

        PostComment comment = post.findCommentById(id).orElseThrow();

        if (!postService.deleteComment(post, comment)) return null;

        return new RsData<>(
                "200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(comment.getId())
        );
    }

    public record PostCommentUpdateReqBody(
            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RsData<PostCommentDto> update(
            @PathVariable int postId,
            @PathVariable int id,
            @RequestBody @Valid PostCommentUpdateReqBody body
    ) {
        Post post = postService.findById(postId).orElseThrow();

        PostComment comment = post.findCommentById(id).orElseThrow();

        comment.modify(body.content);

        return new RsData<>(
                "200-1",
                "%d번 댓글이 수정되었습니다.".formatted(comment.getId()),
                new PostCommentDto(comment)
        );
    }
}