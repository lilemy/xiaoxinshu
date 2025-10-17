package com.lilemy.xiaoxinshu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 全局异常处理类
 *
 * @author lilemy
 * @date 2025-07-21 11:29
 */
@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        // 过滤并格式化堆栈信息
        String stackTrace = getStackTrace(e);
        log.error("业务异常: {} {}\n   {}", e.getCode(), e.getMessage(), stackTrace);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e,
                                                                  HttpServletRequest request) {
        // 获取 BindingResult
        BindingResult bindingResult = e.getBindingResult();
        // 获取请求参数
        Object targetObject = bindingResult.getTarget();
        String reqParam = JSONUtil.toJsonStr(targetObject);
        // 获取错误信息
        String msg = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("，"));
        // 生成请求唯一 id
        String requestId = IdUtil.fastSimpleUUID();
        String path = request.getRequestURI();
        String method = request.getMethod();
        String url = method + " " + path;
        String remoteHost = request.getRemoteHost();
        log.error("请求参数异常 => ID:[{}], URL[{}], IP:[{}], 参数:[{}], 异常信息:[{} {}]",
                requestId, url, remoteHost, reqParam, ResultCode.PARAMS_ERROR.getCode(), msg);
        return ResultUtils.error(ResultCode.PARAMS_ERROR, msg);
    }

    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<?> notRoleExceptionHandler(RuntimeException e) {
        // 过滤并格式化堆栈信息
        String stackTrace = getStackTrace(e);
        log.error("无权限异常 :{}\n   {}", e.getMessage(), stackTrace);
        return ResultUtils.error(ResultCode.NO_AUTH_ERROR, "无权限");
    }

    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginExceptionHandler(RuntimeException e) {
        // 过滤并格式化堆栈信息
        String stackTrace = getStackTrace(e);
        log.error("未登录异常 :{}\n   {}", e.getMessage(), stackTrace);
        return ResultUtils.error(ResultCode.NOT_LOGIN_ERROR, "未登录");
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常", e);
        return ResultUtils.error(ResultCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("系统异常", e);
        return ResultUtils.error(ResultCode.SYSTEM_ERROR, "系统错误");
    }

    /**
     * 过滤并格式化堆栈信息
     *
     * @param throwable 异常
     * @return 堆栈信息
     */
    private static String getStackTrace(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .filter(line -> line.contains("com.lilemy.xiaoxinshu"))
                .collect(Collectors.joining("\n   "));
    }

}
