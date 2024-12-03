package cn.lilemy.xiaoxinshucommon.exception;


import cn.lilemy.xiaoxinshucommon.common.ResultCode;

/**
 * 抛异常工具类
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        判断条件
     * @param runtimeException 运行异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 判断条件
     * @param errorCode 异常状态码
     */
    public static void throwIf(boolean condition, ResultCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 判断条件
     * @param errorCode 异常状态码
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, ResultCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
