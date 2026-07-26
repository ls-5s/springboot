package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

// 用户信息响应
@Data
@Builder
public class UserInfoVO {

    private Long id;         // 用户 ID
    private String username; // 用户名
    private String nickname; // 昵称
    private String email;    // 邮箱
    private String avatar;   // 头像 URL
    private Integer status;  // 0禁用 1正常
}
