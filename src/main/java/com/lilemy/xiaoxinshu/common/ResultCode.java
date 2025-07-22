package com.lilemy.xiaoxinshu.common;

import lombok.Getter;

/**
 * 状态码枚举类
 *
 * @author lilemy
 * @date 2025-07-21 09:43
 */
@Getter
public enum ResultCode {

    SUCCESS(0, "操作成功"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    FREQUENT_ERROR(40110, "请求频繁"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
