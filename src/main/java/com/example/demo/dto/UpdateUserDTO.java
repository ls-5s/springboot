package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

// 修改个人信息请求
@Data
public class UpdateUserDTO {

    @Email(message = "邮箱格式不正确")
    private String email;    // 邮箱

    private String nickname; // 昵称

    private String avatar;   // 头像 URL
}
