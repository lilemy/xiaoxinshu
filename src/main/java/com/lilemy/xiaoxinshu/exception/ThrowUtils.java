package com.lilemy.xiaoxinshu.exception;

import com.lilemy.xiaoxinshu.common.ResultCode;

/**
 * 抛异常工具类
 *
 * @author lilemy
 * @date 2025-07-21 14:28
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param code      错误码
     */
    public static void throwIf(boolean condition, ResultCode code) {
        throwIf(condition, new BusinessException(code));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param code      错误码
     * @param message   错误信息
     */
    public static void throwIf(boolean condition, ResultCode code, String message) {
        throwIf(condition, new BusinessException(code, message));
    }

}
