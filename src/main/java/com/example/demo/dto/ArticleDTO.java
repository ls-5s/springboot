package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

// 新增/修改文章请求
@Data
public class ArticleDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最长 100 字")
    private String title;           // 标题

    @Size(max = 500, message = "摘要最长 500 字")
    private String summary;         // 摘要

    private String content;         // 正文 (Markdown)

    private String cover;           // 封面图 URL

    private Long categoryId;        // 分类 ID

    private List<Long> tagIds;      // 标签 ID 列表

    private Integer status;         // 0草稿 1发布 2私密

    private Integer isTop;          // 是否置顶
}
