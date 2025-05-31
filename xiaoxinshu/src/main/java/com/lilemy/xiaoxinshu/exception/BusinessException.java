package com.lilemy.xiaoxinshu.exception;

import com.lilemy.xiaoxinshu.common.ResultCode;
import lombok.Getter;

/**
 * 自定义异常
 *
 * @author lilemy
 * @date 2025/05/27 23:24
 */
@Getter
public class BusinessException extends RuntimeException {

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

