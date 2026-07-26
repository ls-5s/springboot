package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 发表评论请求
@Data
public class CommentDTO {

    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;      // 文章 ID

    private Long parentId;       // 父评论 ID（回复时传）

    private String visitorName;  // 游客昵称（未登录时传）

    @NotBlank(message = "评论内容不能为空")
    private String content;      // 评论内容
}
