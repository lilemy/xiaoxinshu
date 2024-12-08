package cn.lilemy.xiaoxinshucommon.exception;


import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 自定义异常
 */
@Getter
public class BusinessException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 5915032392946288013L;
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

}
