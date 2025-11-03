package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(name = "SysUserRegisterRequest", description = "用户注册请求")
public class SysUserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5181362380016156551L;

    /**
     * 账号
     */
    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 16, message = "账号长度在4到16之间")
    private String userAccount;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度在8到32之间")
    private String userPassword;

    /**
     * 确认密码
     */
    @Schema(description = "确认密码")
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 8, max = 32, message = "确认密码长度在8到32之间")
    private String checkPassword;
}
