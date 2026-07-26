package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.Result;
import com.example.demo.dto.ArchiveVO;
import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.ArticleVO;
import com.example.demo.model.entity.Article;
import com.example.demo.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 文章控制器：列表（公开）、发布/修改/删除（需要登录）
@Tag(name = "文章接口")
@Slf4j
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // GET /api/articles — 文章列表（公开，分页 + 筛选）
    @Operation(summary = "文章列表")
    @GetMapping
    public Result<IPage<Article>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String keyword) {
        IPage<Article> result = articleService.getArticleList(page, size, categoryId, tagId, keyword);
        return Result.success(result);
    }

    // GET /api/articles/{id} — 文章详情（公开）
    @Operation(summary = "文章详情")
    @GetMapping("/{id}")
    public Result<ArticleVO> detail(@PathVariable Long id) {
        ArticleVO vo = articleService.getArticleDetail(id);
        return Result.success(vo);
    }

    // GET /api/articles/archive — 文章归档（公开，注：必须在 /{id} 之前注册）
    @Operation(summary = "文章归档")
    @GetMapping("/archive")
    public Result<List<ArchiveVO>> archive() {
        List<ArchiveVO> list = articleService.getArchive();
        return Result.success(list);
    }

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
