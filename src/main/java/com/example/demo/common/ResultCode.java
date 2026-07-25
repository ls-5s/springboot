package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 业务状态码枚举
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),           // 请求成功
    FAIL(500, "操作失败"),              // 服务器内部错误
    UNAUTHORIZED(401, "未授权"),        // 未登录 / Token 过期
    FORBIDDEN(403, "无权限"),           // 已登录但权限不足
    NOT_FOUND(404, "资源不存在"),        // 请求的数据不存在
    VALIDATE_FAILED(400, "参数校验失败"), // 请求参数不符合要求
    BUSINESS_ERROR(510, "业务异常");     // 自定义业务异常

    private final int code;
    private final String message;
}
