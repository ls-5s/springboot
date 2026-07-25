package com.example.demo.common.exception;

import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    // 捕获参数校验异常（@Valid 失败）
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidException(Exception e) {
        String message = "参数校验失败";
        if (e instanceof MethodArgumentNotValidException ex) {
            // @RequestBody 校验失败
            var bindingResult = ex.getBindingResult();
            if (bindingResult.hasFieldErrors()) {
                message = bindingResult.getFieldError().getDefaultMessage();
            }
        } else if (e instanceof BindException ex) {
            // @ModelAttribute 校验失败
            if (ex.hasFieldErrors()) {
                message = ex.getFieldError().getDefaultMessage();
            }
        }
        return Result.fail(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    // 兜底：捕获所有未处理的异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail(ResultCode.FAIL.getCode(), "系统异常，请联系管理员");
    }
}
