package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.dto.UserInfoVO;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// 用户控制器：查询和修改个人信息（需要登录）
@Tag(name = "用户接口")
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/user/info — 获取当前登录用户信息
    @Operation(summary = "当前用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        Long userId = getCurrentUserId();
        log.info("【查询用户信息】userId={}", userId);
        UserInfoVO vo = userService.getCurrentUser(userId);
        return Result.success(vo);
    }

    // PUT /api/user/info — 修改当前登录用户信息
    @Operation(summary = "修改个人信息")
    @PutMapping("/info")
    public Result<Void> updateInfo(@Valid @RequestBody UpdateUserDTO dto) {
        Long userId = getCurrentUserId();
        log.info("【修改用户信息】userId={}", userId);
        userService.updateUserInfo(userId, dto);
        return Result.success();
    }

    // 从 SecurityContext 获取当前登录用户 ID
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(principal.toString());
    }
}
