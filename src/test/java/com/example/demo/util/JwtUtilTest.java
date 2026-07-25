package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// JWT 工具类单元测试
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("test-secret-key-for-unit-test-123456", 3600);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generate(1L, "test");
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testParseToken() {
        String token = jwtUtil.generate(2L, "user2");
        Claims claims = jwtUtil.parse(token);
        assertEquals("2", claims.getSubject());        // userId
        assertEquals("user2", claims.get("username")); // username
    }

    @Test
    void testExpiredToken() {
        // 生成一个已过期的 token（-1 秒）
        JwtUtil shortLive = new JwtUtil("test-secret-key-for-unit-test-123456", -1);
        String token = shortLive.generate(1L, "test");
        assertThrows(ExpiredJwtException.class, () -> shortLive.parse(token));
    }
}
