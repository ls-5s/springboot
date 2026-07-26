package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 评论响应
@Data
@Builder
public class CommentVO {

    private Long id;
    private Long userId;
    private String userName;         // 登录用户昵称 或 游客昵称
    private String avatar;           // 用户头像
    private String content;
    private LocalDateTime createTime;
    private List<CommentVO> children; // 子回复
}
