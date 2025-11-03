package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户更新请求（仅管理员）
 *
 * @author lilemy
 * @date 2025-10-19 13:39
 */
@Data
@Schema(name = "SysUserUpdateRequest", description = "用户更新请求（仅管理员）")
public class SysUserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -9155006498502527159L;

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "主键不能为空")
    private Long id;

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
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Schema(description = "用户简介")
    private String userProfile;

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

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
