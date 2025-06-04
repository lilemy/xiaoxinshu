package com.lilemy.xiaoxinshu.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author lilemy
 * @date 2025/05/27 23:23
 */
@Data
@Schema(name = "通用返回", description = "通用返回")
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6202072371941160467L;

    @Schema(description = "响应状态码")
    private int code;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应信息")
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
