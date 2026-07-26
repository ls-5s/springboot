package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 配置：定义哪些接口公开、哪些需要认证
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 关闭 CSRF（前后端分离用 JWT，不需要）
            .csrf(csrf -> csrf.disable())
            // 允许跨域（由 CorsConfig 处理）
            .cors(cors -> {})
            // 无状态会话（不创建 HttpSession）
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 路径权限
            .authorizeHttpRequests(auth -> auth
                // 公开接口：登录、注册、测试
                .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/logout").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                // 公开接口：接口文档
                .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 公开接口：Druid 监控
                .requestMatchers("/druid/**").permitAll()
                // 其余所有接口需要携带有效 JWT
                .anyRequest().authenticated()
            )
            // JWT 过滤器在密码认证过滤器之前执行
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
