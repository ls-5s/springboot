package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.LoginVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// AuthController 单元测试（Mock UserService）
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    // ==================== 登录测试 ====================

    @Test
    void testLoginSuccess() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");

        LoginVO vo = LoginVO.builder()
                .token("test-token")
                .userId(1L)
                .username("admin")
                .nickname("管理员")
                .build();
        when(userService.login(any(LoginDTO.class))).thenReturn(vo);

        Result<LoginVO> result = authController.login(dto);

        assertEquals(200, result.getCode());
        assertEquals("test-token", result.getData().getToken());
        assertEquals("admin", result.getData().getUsername());
        verify(userService).login(dto);
    }

    @Test
    void testLoginReturnsCorrectVO() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("user1");
        dto.setPassword("pass1");

        LoginVO vo = LoginVO.builder()
                .token("token-123")
                .userId(10L)
                .username("user1")
                .nickname("用户一")
                .build();
        when(userService.login(dto)).thenReturn(vo);

        Result<LoginVO> result = authController.login(dto);

        assertEquals(10L, result.getData().getUserId());
        assertEquals("用户一", result.getData().getNickname());
    }

    // ==================== 注册测试 ====================

    @Test
    void testRegisterSuccess() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("newuser");
        dto.setPassword("123456");
        dto.setNickname("新用户");

        doNothing().when(userService).register(any(RegisterDTO.class));

        Result<Void> result = authController.register(dto);

        assertEquals(200, result.getCode());
        assertNull(result.getData());
        verify(userService).register(dto);
    }
}
