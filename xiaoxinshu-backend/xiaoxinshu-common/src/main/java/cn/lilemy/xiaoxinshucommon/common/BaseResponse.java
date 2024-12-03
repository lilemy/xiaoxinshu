package cn.lilemy.xiaoxinshucommon.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6202072371941160467L;

    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ResultCode resultCode) {
        this(resultCode.getCode(), null, resultCode.getMessage());
    }

}
