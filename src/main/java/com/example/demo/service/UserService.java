package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.*;
import com.example.demo.model.entity.User;

// 用户服务接口：继承 IService 获得 MyBatis-Plus 通用 CRUD
public interface UserService extends IService<User> {

    // 登录：校验用户名密码，返回 token + 用户信息
    LoginVO login(LoginDTO dto);

    // 注册：创建新用户
    void register(RegisterDTO dto);

    // 查询当前登录用户信息
    UserInfoVO getCurrentUser(Long userId);

    // 修改当前登录用户信息
    void updateUserInfo(Long userId, UpdateUserDTO dto);
}
