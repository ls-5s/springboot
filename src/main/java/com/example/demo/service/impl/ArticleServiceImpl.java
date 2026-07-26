package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.dto.ArticleDTO;
import com.example.demo.model.entity.Article;
import com.example.demo.model.entity.ArticleTag;
import com.example.demo.repository.ArticleMapper;
import com.example.demo.repository.ArticleTagMapper;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleTagMapper articleTagMapper;

    // 发布文章：保存文章 + 关联标签
    @Override
    @Transactional
    public Long createArticle(Long userId, ArticleDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setCover(dto.getCover());
        article.setCategoryId(dto.getCategoryId());
        article.setUserId(userId);
        article.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        article.setIsTop(dto.getIsTop() != null ? dto.getIsTop() : 0);
        save(article);

        saveArticleTags(article.getId(), dto.getTagIds());
        return article.getId();
    }

    // 修改文章：校验归属 → 更新字段 → 重建标签关联
    @Override
    @Transactional
    public void updateArticle(Long id, Long userId, ArticleDTO dto) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        if (!article.getUserId().equals(userId)) {
            throw new BusinessException("无权修改他人文章");
        }

        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setCover(dto.getCover());
        article.setCategoryId(dto.getCategoryId());
        article.setStatus(dto.getStatus());
        article.setIsTop(dto.getIsTop());
        updateById(article);

        // 标签：先删后插
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, id));
        saveArticleTags(id, dto.getTagIds());
    }

    // 删除文章：校验归属 → 逻辑删除
    @Override
    public void deleteArticle(Long id, Long userId) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        if (!article.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人文章");
        }
        removeById(id);
    }

    // 保存文章-标签关联
    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        for (Long tagId : tagIds) {
            ArticleTag at = new ArticleTag();
            at.setArticleId(articleId);
            at.setTagId(tagId);
            articleTagMapper.insert(at);
        }
    }
}
