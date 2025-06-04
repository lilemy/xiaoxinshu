package com.lilemy.xiaoxinshu.model.vo.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lilemy
 * @date 2025/06/03 17:45
 */
@Data
public class LoginUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2839698880577199562L;

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户性别(0男 1女)
     */
    private Integer userGender;

    /**
     * 用户生日
     */
    private LocalDate userBirthday;

    /**
     * 用户角色(user admin)
     */
    private String userRole;

    /**
     * 备注
     */
    private String remark;

    /**
     * 编辑时间
     */
    private LocalDateTime editTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
