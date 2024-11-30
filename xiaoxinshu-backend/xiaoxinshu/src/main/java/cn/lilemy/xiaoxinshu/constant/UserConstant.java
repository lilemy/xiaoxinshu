package cn.lilemy.xiaoxinshu.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    // region 用户默认属性

    /**
     * 用户默认名
     */
    String DEFAULT_NICKNAME = "用户";

    /**
     * 用户默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 用户默认头像
     */
    String DEFAULT_AVATAR = "";

    // endregion

    // region 用户权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion
}
