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
 * @date 2025-07-21 09:45
 */
@Data
@Schema(name = "BaseResponse", description = "通用返回")
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2288345166566483666L;

    /**
     * 响应状态码
     */
    @Schema(description = "响应状态码")
    private int code;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 响应信息
     */
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
