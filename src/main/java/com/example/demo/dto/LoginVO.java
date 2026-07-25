package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

// 登录响应：返回 token 和用户基本信息
@Data
@Builder
public class LoginVO {

    private String token;      // JWT token
    private Long userId;       // 用户 ID
    private String username;   // 用户名
    private String nickname;   // 昵称
}
