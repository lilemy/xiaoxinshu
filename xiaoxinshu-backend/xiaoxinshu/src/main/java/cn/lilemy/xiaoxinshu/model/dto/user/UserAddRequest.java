package cn.lilemy.xiaoxinshu.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员创建用户请求
 */
@Data
public class UserAddRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -5439035398677972510L;
    /**
     * 登录账号
     */
    private String userAccount;
    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
    /**
     * 用户昵称
     */
    private String username;

}
