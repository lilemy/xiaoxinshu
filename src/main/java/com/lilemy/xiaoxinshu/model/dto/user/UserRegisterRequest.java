package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author lilemy
 * @date 2025-10-04 23:41
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5181362380016156551L;

    /**
     * 账号
     */
    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    private String userAccount;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String userPassword;

    /**
     * 确认密码
     */
    @Schema(description = "确认密码")
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}
