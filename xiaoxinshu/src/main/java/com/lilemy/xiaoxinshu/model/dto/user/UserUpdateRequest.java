package com.lilemy.xiaoxinshu.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author lilemy
 * @date 2025/06/03 11:46
 */
@Data
@Schema(name = "UserUpdateRequest", description = "更新用户信息请求")
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -9155006498502527159L;

    /**
     * id
     */
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 密码
     */
    @Schema(description = "用户密码")
    private String userPassword;

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
     * 用户性别(0男 1女)
     */
    @Schema(description = "用户性别(0男 1女)")
    private Integer userGender;

    /**
     * 用户生日
     */
    @Schema(description = "用户生日")
    private LocalDate userBirthday;

    /**
     * 用户角色(user admin)
     */
    @Schema(description = "用户角色(user admin)")
    private String userRole;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
