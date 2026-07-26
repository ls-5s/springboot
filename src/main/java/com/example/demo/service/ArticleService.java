package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.ArchiveVO;
import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.ArticleVO;
import com.example.demo.model.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    // 文章列表（公开，分页+筛选）
    IPage<Article> getArticleList(int page, int size, Long categoryId, Long tagId, String keyword);

    // 文章详情（公开）
    ArticleVO getArticleDetail(Long id);

    // 文章归档（公开）
    List<ArchiveVO> getArchive();

    // 发布文章
    Long createArticle(Long userId, ArticleDTO dto);

    // 修改文章
    void updateArticle(Long id, Long userId, ArticleDTO dto);

    // 删除文章（逻辑删除）
    void deleteArticle(Long id, Long userId);
}
