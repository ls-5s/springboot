package com.example.demo.common.exception;

import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 全局异常处理：拦截所有异常，统一转成 Result 格式返回
@Slf4j
// @RestControllerAdvice = @ControllerAdvice + @ResponseBody，自动转 JSON
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 捕获参数校验异常（@Valid 失败），一次性返回全部字段错误
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Map<String, String>> handleValidException(Exception e) {
        List<FieldError> fieldErrors;
        if (e instanceof MethodArgumentNotValidException ex) {
            fieldErrors = ex.getBindingResult().getFieldErrors();
        } else {
            fieldErrors = ((BindException) e).getFieldErrors();
        }

        // 按字段顺序收集，保留第一个校验失败信息
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : fieldErrors) {
            errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }

        log.warn("参数校验失败，共 {} 处错误: {}", errors.size(), errors);
        String message = errors.size() == 1
                ? errors.values().iterator().next()
                : String.format("参数校验失败，共 %d 处错误", errors.size());
        return Result.fail(ResultCode.VALIDATE_FAILED.getCode(), message, errors);
    }

    // 兜底：捕获所有未处理的异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail(ResultCode.FAIL.getCode(), "系统异常，请联系管理员");
    }
}
