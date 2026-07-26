package com.example.demo.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.dto.*;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;

    // 登录逻辑：查用户 → 验密码 → 查状态 → 生成 token
    @Override
    public LoginVO login(LoginDTO dto) {
        // 1. 根据用户名查用户
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        // 2. BCrypt 校验密码
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        // 3. 检查账号是否被禁用
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        // 4. 生成 JWT token 并返回
        String token = jwtUtil.generate(user.getId(), user.getUsername());
        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    // 注册逻辑：用户名唯一 → BCrypt 加密密码 → 保存
    @Override
    public void register(RegisterDTO dto) {
        // 1. 检查用户名是否已存在
        long count = count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        // 2. 保存用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword())); // BCrypt 加密
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setStatus(1);
        save(user);
    }

    // 查询当前用户信息
    @Override
    public UserInfoVO getCurrentUser(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .build();
    }

    // 修改当前用户信息：只更新昵称、邮箱、头像
    @Override
    public void updateUserInfo(Long userId, UpdateUserDTO dto) {
        User user = new User();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        updateById(user);
    }
}
