package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author lilemy
 * @date 2025/06/03 11:04
 */
@Data
@Schema(name = "UserRegisterRequest", description = "用户注册请求")
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4267477356000515919L;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userAccount;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userPassword;

    /**
     * 确认密码
     */
    @Schema(description = "确认密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String checkPassword;

}
