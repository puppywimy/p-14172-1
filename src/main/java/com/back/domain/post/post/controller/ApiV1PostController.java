package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @GetMapping
    public List<PostDto> list() {
        List<Post> posts = postService.findAll();

        return posts.stream().map(PostDto::new).toList();
    }

    @GetMapping("/{id}")
    public PostDto read(@PathVariable int id) {
        Post post = postService.findById(id).orElseThrow();
        return new PostDto(post);
    }

    public record PostCreateReqBody(
            @NotBlank
            @Size(min = 2, max = 20)
            String title,
            @NotBlank
            @Size(min = 2, max = 5000)
            String content
    ) {
    }

    @PostMapping
    public RsData<PostDto> create(@RequestBody @Valid PostCreateReqBody body) {
        Post post = postService.write(body.title, body.content);
        return new RsData<>(
                "201-1",
                "%d번 글이 작성되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<PostDto> delete(@PathVariable int id) {
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isEmpty()) return null;
        Post post = optionalPost.get();

        postService.delete(post);

        return new RsData<>(
                "200-1",
                "%d번 글이 삭제되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );
    }

    public record PostUpdateReqBody(
            @NotBlank
            @Size(min = 2, max = 20)
            String title,
            @NotBlank
            @Size(min = 2, max = 5000)
            String content
    ) {
    }

    @PutMapping("/{id}")
    @Transactional
    public RsData<PostDto> update(
            @PathVariable int id,
            @RequestBody @Valid PostUpdateReqBody body
    ) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) return null;
        Post post = optionalPost.get();

        postService.modify(post, body.title, body.content);

        return new RsData<>(
                "200-1",
                "%d번 글이 수정되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );
    }
}