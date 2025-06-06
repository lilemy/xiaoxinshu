package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author lilemy
 * @date 2025/06/03 11:46
 */
@Data
@Schema(name = "UserLoginRequest", description = "用户登录请求")
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4009649973332685081L;

    /**
     * 账号
     */
    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userAccount;

    /**
     * 密码
     */
    @Schema(description = "用户密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userPassword;
}
