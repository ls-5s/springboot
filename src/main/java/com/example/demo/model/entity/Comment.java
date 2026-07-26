package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

// 评论实体，对应 comment 表
@Data
@TableName("comment")
public class Comment {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private Long articleId;      // 文章 ID

    private Long userId;         // 评论人 ID (登录用户)

    private Long parentId;       // 父评论 ID

    private String visitorName;  // 游客昵称 (未登录)

    private String content;      // 评论内容

    private Integer status;      // 0待审 1通过 2拒绝

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createTime;
}
