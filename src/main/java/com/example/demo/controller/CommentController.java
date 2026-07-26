package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.CommentVO;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "评论接口")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // GET /api/comments/article/{id} — 某篇文章的评论
    @Operation(summary = "文章评论")
    @GetMapping("/article/{id}")
    public Result<List<CommentVO>> byArticle(@PathVariable Long id) {
        List<CommentVO> list = commentService.getCommentsByArticle(id);
        return Result.success(list);
    }
}
