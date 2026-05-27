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
import org.springframework.http.MediaType;
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
    @DisplayName("POST /posts/1/comments")
    void t1() throws Exception {
        final int postId = 1;

        final ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/posts/%d/comments".formatted(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "content": "댓글 1-4"
                                        }
                                        """)
                )
                .andDo(print());

        final Post post = postService.findById(postId).orElseThrow();
        final PostComment comment = post.getComments().getLast();

        resultActions
                .andExpect(handler().handlerType(ApiV1PostCommentController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%d번 댓글이 생성되었습니다.".formatted(comment.getId())))
                .andExpect(jsonPath("$.data.id").value(comment.getId()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.startsWith(comment.getCreatedAt().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.updatedAt").value(Matchers.startsWith(comment.getUpdatedAt().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.content").value("댓글 1-4"));
    }

    @Test
    @DisplayName("GET /posts/1/comments")
    void t2() throws Exception {
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

    @Test
    @DisplayName("GET /posts/1/comments/1")
    void t3() throws Exception {
        final int postId = 1;
        final int id = 1;

        final ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts/%d/comments/%d".formatted(postId, id))
                )
                .andDo(print());

        final Post post = postService.findById(postId).orElseThrow();
        final PostComment comment = post.findCommentById(id).orElseThrow();

        resultActions
                .andExpect(handler().handlerType(ApiV1PostCommentController.class))
                .andExpect(handler().methodName("read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.createdAt").value(Matchers.startsWith(comment.getCreatedAt().toString().substring(0, 20))))
                .andExpect(jsonPath("$.updatedAt").value(Matchers.startsWith(comment.getUpdatedAt().toString().substring(0, 20))))
                .andExpect(jsonPath("$.content").value(comment.getContent()));
    }

    @Test
    @DisplayName("DELETE /posts/1/comments/1")
    void t4() throws Exception {
        final int postId = 1;
        final int id = 1;

        final ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/posts/%d/comments/%d".formatted(postId, id))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PostCommentController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 댓글이 삭제되었습니다.".formatted(id)));
    }

    @Test
    @DisplayName("PUT /posts/1/comments/1")
    void t5() throws Exception {
        final int postId = 1;
        final int id = 1;

        final ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/%d/comments/%d".formatted(postId, id))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "content": "댓글 1-1 new"
                                        }
                                        """)
                )
                .andDo(print());

        final Post post = postService.findById(postId).orElseThrow();
        final PostComment comment = post.findCommentById(id).orElseThrow();

        resultActions
                .andExpect(handler().handlerType(ApiV1PostCommentController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 댓글이 수정되었습니다.".formatted(id)))
                .andExpect(jsonPath("$.data.id").value(comment.getId()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.startsWith(comment.getCreatedAt().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.updatedAt").value(Matchers.startsWith(comment.getUpdatedAt().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.content").value(comment.getContent()));
    }
}
