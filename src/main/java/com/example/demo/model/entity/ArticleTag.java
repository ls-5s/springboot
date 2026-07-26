package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// 文章-标签关联实体，对应 article_tag 表
@Data
@TableName("article_tag")
public class ArticleTag {

    private Long articleId;  // 文章 ID

    private Long tagId;      // 标签 ID
}
