package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 文章详情响应
@Data
@Builder
public class ArticleVO {

    private Long id;
    private String title;
    private String summary;
    private String content;
    private String cover;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String authorName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer status;
    private Integer isTop;
    private List<TagVO> tags;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Data
    @Builder
    public static class TagVO {
        private Long id;
        private String name;
    }
}
