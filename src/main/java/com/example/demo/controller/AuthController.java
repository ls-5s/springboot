package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.LoginVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 认证控制器：登录 + 注册（公开接口，无需登录）
@Tag(name = "认证接口")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // POST /api/auth/login — 用户名密码登录，返回 JWT token
    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        log.info("【登录请求】username={}", dto.getUsername());
        LoginVO result = userService.login(dto);
        log.info("【登录成功】userId={} username={}", result.getUserId(), result.getUsername());
        return Result.success(result);
    }

    // POST /api/auth/register — 注册新用户
    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        log.info("【注册请求】username={} nickname={}", dto.getUsername(), dto.getNickname());
        userService.register(dto);
        log.info("【注册成功】username={}", dto.getUsername());
        return Result.success();
    }
}
