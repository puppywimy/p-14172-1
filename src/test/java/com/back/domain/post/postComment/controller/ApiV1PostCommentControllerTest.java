package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.entity.PostComment;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiV1PostCommentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("GET /posts/1/comments")
    void t1() throws Exception {
        final int postId = 1;

        final ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts/%d/comments".formatted(postId))
                )
                .andDo(print());

        final Post post = postService.findById(postId).orElseThrow();
        final List<PostComment> comments = post.getComments();
        final int size = comments.size();

        resultActions
                .andExpect(handler().handlerType(ApiV1PostCommentController.class))
                .andExpect(handler().methodName("list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(size));

        for (int i = 0; i < size; i++) {
            PostComment comment = comments.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$[%d].createdAt".formatted(i)).value(Matchers.startsWith(comment.getCreatedAt().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].updatedAt".formatted(i)).value(Matchers.startsWith(comment.getUpdatedAt().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].content".formatted(i)).value(comment.getContent()));
        }
    }
}
