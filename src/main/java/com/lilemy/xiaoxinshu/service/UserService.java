package com.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.model.dto.user.*;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.model.vo.user.LoginUserVo;
import com.lilemy.xiaoxinshu.model.vo.user.UserByAdminVo;
import com.lilemy.xiaoxinshu.model.vo.user.UserVo;

/**
 * 用户服务
 *
 * @author lilemy
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-08-13 11:34:58
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param req 用户注册请求
     * @return 新用户id
     */
    Long userRegister(UserRegisterRequest req);

    /**
     * 用户登录
     *
     * @param req 用户登录请求
     * @return 脱敏后的用户信息
     */
    LoginUserVo userLogin(UserLoginRequest req);

    /**
     * 用户注销
     *
     * @return 是否注销成功
     */
    Boolean userLogout();

    /**
     * 创建用户
     *
     * @param req 用户创建请求体
     * @return 新用户id
     */
    Long createUser(UserCreateRequest req);

    /**
     * 更新用户
     *
     * @param req 用户更新请求体
     * @return 是否更新成功
     */
    Boolean updateUser(UserUpdateRequest req);

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    Boolean deleteUser(Long id);

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    User getLoginUser();

    /**
     * 获取用户脱敏信息
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    UserVo getUserVo(User user);

    /**
     * 获取用户脱敏信息
     *
     * @param userId 用户id
     * @return 脱敏后的用户信息
     */
    UserVo getUserVo(Long userId);

    /**
     * 获取登录用户脱敏信息
     *
     * @param user 用户信息
     * @return 脱敏后的登录用户信息
     */
    LoginUserVo getLoginuserVo(User user);

    /**
     * 获取用户脱敏信息（仅管理员）
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    UserByAdminVo getUserByAdminVo(User user);

    /**
     * 获取用户脱敏信息（仅管理员）
     *
     * @param req       用户查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的用户信息
     */
    Page<UserByAdminVo> getUserByAdminVoPage(UserQueryRequest req, PageQuery pageQuery);

    /**
     * 判断当前登录用户是否为管理员
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
     * 获取查询条件
     *
     * @param req 用户查询请求体
     * @return 查询条件
     */
    LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest req);
}
