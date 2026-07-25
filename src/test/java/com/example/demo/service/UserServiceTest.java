package com.example.demo.service;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.LoginVO;
import com.example.demo.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// 用户服务集成测试（MySQL demo3_test 数据库 + @Transactional 自动回滚）
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    // ==================== 注册 ====================

    @Test
    void testRegisterSuccess() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("123456");
        dto.setNickname("测试用户");
        dto.setEmail("test@test.com");
        System.out.println("【注册-输入】" + dto);

        userService.register(dto);
        System.out.println("【注册-输出】注册成功");
    }

    @Test
    void testRegisterDuplicateUsername() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");
        dto.setNickname("管理员");
        userService.register(dto);

        System.out.println("【注册-输入】重复用户名: admin");
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.register(dto));
        System.out.println("【注册-输出】" + ex.getMessage());
    }

    // ==================== 登录 ====================

    @Test
    void testLoginSuccess() {
        RegisterDTO regDto = new RegisterDTO();
        regDto.setUsername("logintest");
        regDto.setPassword("123456");
        regDto.setNickname("登录测试");
        userService.register(regDto);

        LoginDTO dto = new LoginDTO();
        dto.setUsername("logintest");
        dto.setPassword("123456");
        System.out.println("【登录-输入】" + dto);

        LoginVO result = userService.login(dto);
        System.out.println("【登录-输出】token=" + result.getToken());
        System.out.println("【登录-输出】userId=" + result.getUserId() + " username=" + result.getUsername() + " nickname=" + result.getNickname());

        assertNotNull(result.getToken());
        assertEquals("logintest", result.getUsername());
    }

    @Test
    void testLoginWrongPassword() {
        RegisterDTO regDto = new RegisterDTO();
        regDto.setUsername("pwdtest");
        regDto.setPassword("123456");
        regDto.setNickname("密码测试");
        userService.register(regDto);

        LoginDTO dto = new LoginDTO();
        dto.setUsername("pwdtest");
        dto.setPassword("wrong");
        System.out.println("【登录-输入】" + dto);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.login(dto));
        System.out.println("【登录-输出】" + ex.getMessage());
    }

    @Test
    void testLoginUserNotFound() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("notexist");
        dto.setPassword("123456");
        System.out.println("【登录-输入】" + dto);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.login(dto));
        System.out.println("【登录-输出】" + ex.getMessage());
    }
}
