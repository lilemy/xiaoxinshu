package com.lilemy.xiaoxinshu.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lilemy
 * @date 2025/06/03 18:48
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String s) {
        return new ArrayList<>();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String s) {
        // 从当前登录用户信息中获取角色
        User user = (User) StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        return Collections.singletonList(user.getUserRole());
    }

}
