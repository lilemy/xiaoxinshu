package cn.lilemy.xiaoxinshu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ResultCode.SYSTEM_ERROR, "系统错误");
    }

    // region Sa-Token异常处理
    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<?> notRoleExceptionHandler(RuntimeException e) {
        log.error("NotRoleException", e);
        return ResultUtils.error(ResultCode.NO_AUTH_ERROR, "无权限");
    }

    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginExceptionHandler(RuntimeException e) {
        log.error("NotLoginException", e);
        return ResultUtils.error(ResultCode.NOT_LOGIN_ERROR, "未登录");
    }

    // endregion

}
