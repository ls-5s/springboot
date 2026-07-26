package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

// 文章实体，对应 article 表
@Data
@TableName("article")
public class Article {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String title;        // 标题

    private String summary;      // 摘要

    private String content;      // 正文 (Markdown)

    private String cover;        // 封面图 URL

    private Long categoryId;     // 分类 ID

    private Long userId;         // 作者 ID

    private Integer viewCount;   // 浏览量

    private Integer likeCount;   // 点赞数

    private Integer commentCount; // 评论数

    private Integer status;      // 0草稿 1发布 2私密

    private Integer isTop;       // 是否置顶

    @TableField(fill = FieldFill.INSERT)        // 插入时自动填充
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;

    @TableLogic // 逻辑删除
    private Integer deleted;
}
