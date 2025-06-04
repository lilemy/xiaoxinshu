package com.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.xiaoxinshu.model.dto.user.*;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.model.vo.user.LoginUserVO;
import com.lilemy.xiaoxinshu.model.vo.user.UserVO;

/**
 * @author lilemy
 * @description 针对表【user(用户)】的数据库操作Service
 * @date 2025-06-03 10:54:45
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 新用户id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 用户注销
     *
     * @return 是否注销成功
     */
    Boolean userLogout();

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    User getLoginUser();

    /**
     * 获取脱敏用户信息
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏用户信息
     *
     * @param userId 用户id
     * @return 脱敏后的用户信息
     */
    UserVO getUserVO(Long userId);

    /**
     * 判断当前用户是否为管理员
     *
     * @return 是否为管理员
     */
    Boolean isAdmin();

    /**
     * 判断当前用户是否为管理员
     *
     * @param user 用户信息
     * @return 是否为管理员
     */
    Boolean isAdmin(User user);

    /**
     * 创建用户
     *
     * @param userCreateRequest 用户创建请求体
     * @return 新用户id
     */
    Long createUser(UserCreateRequest userCreateRequest);

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 用户更新请求体
     * @return 是否更新成功
     */
    Boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 编辑用户信息
     *
     * @param userEditRequest 用户编辑请求体
     * @return 是否编辑成功
     */
    Boolean editUser(UserEditRequest userEditRequest);

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    Boolean deleteUser(Long id);

    /**
     * 获取脱敏用户信息
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取加密密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户查询请求体
     * @return 查询条件
     */
    LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 校验用户信息
     *
     * @param user 用户信息
     * @param add  是否添加
     */
    void validUser(User user, boolean add);
}
