package cn.lilemy.xiaoxinshu.model.dto.user;

import cn.lilemy.xiaoxinshucommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2001052209766375292L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录账号
     */
    private String userAccount;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
