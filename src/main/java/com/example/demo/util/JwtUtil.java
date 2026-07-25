package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 工具类：生成和解析 JWT token
@Component
public class JwtUtil {

    private final SecretKey key; // 签名密钥
    private final long expire;   // 过期时间（秒）

    // 从 application.yml 读取 jwt.secret 和 jwt.expire
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expire}") long expire) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expire = expire;
    }

    // 生成 JWT token，payload 存 userId 和 username
    public String generate(Long userId, String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expire * 1000))
                .signWith(key)
                .compact();
    }

    // 解析 JWT token，返回 Claims（可从中获取 userId、username）
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
