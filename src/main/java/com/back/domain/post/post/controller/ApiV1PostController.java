package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
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
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isEmpty()) return null;
        Post post = optionalPost.get();

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

    public record PostCreateResBody(
            long totalCount,
            PostDto post
    ) {
    }

    @PostMapping
    public RsData<PostCreateResBody> create(@RequestBody @Valid PostCreateReqBody body) {
        Post post = postService.write(body.title, body.content);
        return new RsData<>(
                "200-1",
                "%d번 글이 작성되었습니다.".formatted(post.getId()),
                new PostCreateResBody(
                        postService.count(),
                        new PostDto(post)
                )
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
}