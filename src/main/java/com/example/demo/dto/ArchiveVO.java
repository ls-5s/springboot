package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

// 文章归档响应（按年月分组）
@Data
@Builder
public class ArchiveVO {

    private String yearMonth;  // 2026-07
    private Integer count;     // 当月文章数
    private List<Item> articles;

    @Data
    @Builder
    public static class Item {
        private Long id;
        private String title;
        private String createTime;
    }
}
