package com.example.demo.controller;

import com.example.demo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "测试接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "Hello 测试")
    @GetMapping("/hello")
    public Result<String> hello() {
        log.info("测试接口被调用");
        log.debug("debug 级别日志（只在 dev 环境输出）");
        return Result.success("Hello, Spring Boot 3!");
    }
}
