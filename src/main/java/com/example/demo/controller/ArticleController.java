package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.ArticleDTO;
import com.example.demo.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// 文章控制器：发布、修改、删除（需要登录）
@Tag(name = "文章接口")
@Slf4j
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // POST /api/articles — 发布文章
    @Operation(summary = "发布文章")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody ArticleDTO dto) {
        Long userId = getCurrentUserId();
        log.info("【发布文章】userId={} title={}", userId, dto.getTitle());
        Long id = articleService.createArticle(userId, dto);
        log.info("【发布成功】articleId={}", id);
        return Result.success(id);
    }

    // PUT /api/articles/{id} — 修改文章
    @Operation(summary = "修改文章")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ArticleDTO dto) {
        Long userId = getCurrentUserId();
        log.info("【修改文章】articleId={} userId={}", id, userId);
        articleService.updateArticle(id, userId, dto);
        log.info("【修改成功】articleId={}", id);
        return Result.success();
    }

    // DELETE /api/articles/{id} — 删除文章
    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        log.info("【删除文章】articleId={} userId={}", id, userId);
        articleService.deleteArticle(id, userId);
        log.info("【删除成功】articleId={}", id);
        return Result.success();
    }

    // 从 SecurityContext 获取当前登录用户 ID
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(principal.toString());
    }
}
