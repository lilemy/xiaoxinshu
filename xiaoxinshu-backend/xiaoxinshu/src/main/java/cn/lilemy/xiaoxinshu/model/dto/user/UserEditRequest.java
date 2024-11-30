package cn.lilemy.xiaoxinshu.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户编辑请求体（用户自己修改）
 */
@Data
public class UserEditRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2128280749100721877L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

}
