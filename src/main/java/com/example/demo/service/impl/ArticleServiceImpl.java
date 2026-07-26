package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.dto.ArchiveVO;
import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.ArticleVO;
import com.example.demo.model.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleTagMapper articleTagMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;

    // 文章列表：只查已发布，置顶优先 + 时间倒序
    @Override
    public IPage<Article> getArticleList(int page, int size, Long categoryId, Long tagId, String keyword) {
        // 按标签筛选时，先查关联表获取文章 ID 列表
        if (tagId != null) {
            List<ArticleTag> relations = articleTagMapper.selectList(
                    new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tagId));
            List<Long> articleIds = relations.stream()
                    .map(ArticleTag::getArticleId).collect(Collectors.toList());
            if (articleIds.isEmpty()) {
                return new Page<>(page, size); // 无匹配文章，返回空页
            }
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                    .in(Article::getId, articleIds)
                    .eq(Article::getStatus, 1)
                    .eq(categoryId != null, Article::getCategoryId, categoryId)
                    .like(StringUtils.hasText(keyword), Article::getTitle, keyword)
                    .orderByDesc(Article::getIsTop)
                    .orderByDesc(Article::getCreateTime);
            return page(new Page<>(page, size), wrapper);
        }

        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, 1)
                .eq(categoryId != null, Article::getCategoryId, categoryId)
                .like(StringUtils.hasText(keyword), Article::getTitle, keyword)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }

    // 文章详情：文章 + 分类名 + 作者昵称 + 标签列表
    @Override
    public ArticleVO getArticleDetail(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 分类名称
        String categoryName = null;
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            categoryName = category != null ? category.getName() : null;
        }

        // 作者昵称
        String authorName = null;
        if (article.getUserId() != null) {
            User user = userMapper.selectById(article.getUserId());
            authorName = user != null ? (user.getNickname() != null ? user.getNickname() : user.getUsername()) : null;
        }

        // 标签列表
        List<ArticleVO.TagVO> tags = Collections.emptyList();
        List<ArticleTag> relations = articleTagMapper.selectList(
                new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, id));
        if (!relations.isEmpty()) {
            List<Long> tagIds = relations.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
            List<Tag> tagList = tagMapper.selectBatchIds(tagIds);
            tags = tagList.stream()
                    .map(t -> ArticleVO.TagVO.builder().id(t.getId()).name(t.getName()).build())
                    .collect(Collectors.toList());
        }

        return ArticleVO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .summary(article.getSummary())
                .content(article.getContent())
                .cover(article.getCover())
                .categoryId(article.getCategoryId())
                .categoryName(categoryName)
                .userId(article.getUserId())
                .authorName(authorName)
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .commentCount(article.getCommentCount())
                .status(article.getStatus())
                .isTop(article.getIsTop())
                .tags(tags)
                .createTime(article.getCreateTime())
                .updateTime(article.getUpdateTime())
                .build();
    }

    // 文章归档：按年月分组统计
    @Override
    public List<ArchiveVO> getArchive() {
        List<Article> articles = list(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getCreateTime));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, List<Article>> grouped = articles.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCreateTime().format(fmt),
                        LinkedHashMap::new,
                        Collectors.toList()));

        List<ArchiveVO> result = new ArrayList<>();
        for (Map.Entry<String, List<Article>> entry : grouped.entrySet()) {
            List<ArchiveVO.Item> items = entry.getValue().stream()
                    .map(a -> ArchiveVO.Item.builder()
                            .id(a.getId())
                            .title(a.getTitle())
                            .createTime(a.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .build())
                    .collect(Collectors.toList());
            result.add(ArchiveVO.builder()
                    .yearMonth(entry.getKey())
                    .count(items.size())
                    .articles(items)
                    .build());
        }
        return result;
    }

    // 管理端文章列表：含草稿，按状态筛选
    @Override
    public IPage<Article> getAdminArticleList(int page, int size, String keyword, Integer status) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .eq(status != null, Article::getStatus, status)
                .like(StringUtils.hasText(keyword), Article::getTitle, keyword)
                .orderByDesc(Article::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }

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
