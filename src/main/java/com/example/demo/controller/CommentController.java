package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.CommentVO;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评论接口")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // GET /api/comments/article/{id} — 某篇文章的评论（公开）
    @Operation(summary = "文章评论")
    @GetMapping("/article/{id}")
    public Result<List<CommentVO>> byArticle(@PathVariable Long id) {
        List<CommentVO> list = commentService.getCommentsByArticle(id);
        return Result.success(list);
    }

    // POST /api/comments — 发表评论（需登录）
    @Operation(summary = "发表评论")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody CommentDTO dto) {
        Long userId = getCurrentUserId();
        commentService.createComment(userId, dto);
        return Result.success();
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(principal.toString());
    }
}
