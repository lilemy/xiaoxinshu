package com.lilemy.xiaoxinshu.exception;

import com.lilemy.xiaoxinshu.common.ResultCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义业务异常
 *
 * @author lilemy
 * @date 2025-07-21 11:25
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3395398453984456947L;

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode code) {
        super(code.getMessage());
        this.code = code.getCode();
    }

    public BusinessException(ResultCode code, String message) {
        super(message);
        this.code = code.getCode();
    }

}
