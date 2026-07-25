package com.example.demo.common.exception;

import lombok.Getter;

// 自定义业务异常：Service 层遇到异常直接 throw，由 GlobalExceptionHandler 统一处理
@Getter
public class BusinessException extends RuntimeException {

    private final int code; // 异常状态码

    // 默认状态码 500
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    // 自定义状态码
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
