package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户查询请求
 *
 * @author lilemy
 * @date 2025-10-07 21:12
 */
@Data
@Schema(name = "SysUserQueryRequest", description = "用户查询请求")
public class SysUserQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5841117757298107476L;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String userAccount;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String userName;

    /**
     * 用户手机号
     */
    @Schema(description = "用户手机号")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String userEmail;

    /**
     * 用户性别(0未知 1男 2女)
     */
    @Schema(description = "用户性别(0未知 1男 2女)")
    private Integer userGender;

    /**
     * 用户生日
     */
    @Schema(description = "用户生日")
    private LocalDate userBirthday;

    /**
     * 用户角色(0用户 1管理员)
     */
    @Schema(description = "用户角色(0用户 1管理员)")
    private Integer userRole;
}
