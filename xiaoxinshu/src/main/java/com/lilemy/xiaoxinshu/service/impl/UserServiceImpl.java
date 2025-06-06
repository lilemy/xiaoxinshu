package com.lilemy.xiaoxinshu.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.constant.CommonConstant;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.exception.BusinessException;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.mapper.UserMapper;
import com.lilemy.xiaoxinshu.model.dto.user.*;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.model.enums.UserRoleEnum;
import com.lilemy.xiaoxinshu.model.vo.user.LoginUserVO;
import com.lilemy.xiaoxinshu.model.vo.user.UserVO;
import com.lilemy.xiaoxinshu.service.UserService;
import com.lilemy.xiaoxinshu.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lilemy
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @date 2025-06-03 10:54:45
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 新用户id
     */
    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "注册参数为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账号长度必须在 4 ~ 20 位之间");
        }
        ThrowUtils.throwIf(Validator.hasChinese(userAccount), ResultCode.PARAMS_ERROR, "用户账号不能包含中文");
        ThrowUtils.throwIf(!Validator.isGeneral(userAccount), ResultCode.PARAMS_ERROR, "用户账号不能包含特殊字符");
        if (userPassword.length() < 6 || userPassword.length() > 20 || checkPassword.length() < 6 || checkPassword.length() > 20) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户密码长度必须在 6 ~ 20 位之间");
        }
        ThrowUtils.throwIf(Validator.hasChinese(userPassword), ResultCode.PARAMS_ERROR, "用户密码不能包含中文");
        ThrowUtils.throwIf(!Validator.isGeneral(userPassword), ResultCode.PARAMS_ERROR, "用户密码不能包含特殊字符");
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 2.校验账户不能重复
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getUserAccount, userAccount);
            if (this.count(lqw) > 0) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账号重复");
            }
            // 3.加密
            String encryptPassword = getEncryptPassword(userPassword);
            // 4.插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            // 默认名称 + 当前时间戳
            user.setUserName(UserConstant.DEFAULT_NICKNAME + System.currentTimeMillis());
            user.setUserRole(UserRoleEnum.USER.getValue());
            boolean save = this.save(user);
            ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "注册失败，数据库异常");
            return user.getId();
        }
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "登录参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账号错误");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户密码错误");
        }
        // 2.查询用户
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserAccount, userAccount);
        User user = this.getOne(lqw);
        // 用户不存在
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR, "用户不存在");
        // 密码是否匹配
        String encryptPassword = getEncryptPassword(userPassword);
        ThrowUtils.throwIf(!encryptPassword.equals(user.getUserPassword()), ResultCode.PARAMS_ERROR, "密码错误");
        // 判断用户是否被封号
        ThrowUtils.throwIf(UserRoleEnum.BAN.getValue().equals(user.getUserRole()), ResultCode.NO_AUTH_ERROR, "您已被封号，请联系管理员！");
        // 4.记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getTokenSession().set(UserConstant.USER_LOGIN_STATE, user);
        return getLoginUserVO(user);
    }

    /**
     * 用户注销
     *
     * @return 是否注销成功
     */
    @Override
    public Boolean userLogout() {
        StpUtil.checkLogin();
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    @Override
    public User getLoginUser() {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ResultCode.NOT_LOGIN_ERROR);
        // 先判断是否已登录
        Object userObj = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 从数据库查询
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ResultCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    /**
     * 获取脱敏用户信息
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        // 隐藏手机号
        if (user.getUserPhone() != null) {
            userVO.setUserPhone(PhoneUtil.hideBetween(user.getUserPhone()).toString());
        }
        return userVO;
    }

    /**
     * 获取脱敏用户信息
     *
     * @param userId 用户id
     * @return 脱敏后的用户信息
     */
    @Override
    public UserVO getUserVO(Long userId) {
        User user = null;
        if (userId != null && userId > 0) {
            user = this.getById(userId);
        }
        return this.getUserVO(user);
    }

    /**
     * 判断当前用户是否为管理员
     *
     * @return 是否为管理员
     */
    @Override
    public Boolean isAdmin() {
        User loginUser = getLoginUser();
        return isAdmin(loginUser);
    }

    /**
     * 判断当前用户是否为管理员
     *
     * @param user 用户信息
     * @return 是否为管理员
     */
    @Override
    public Boolean isAdmin(User user) {
        return user != null && UserConstant.ADMIN.equals(user.getUserRole());
    }

    /**
     * 创建用户
     *
     * @param userCreateRequest 用户创建请求体
     * @return 新用户id
     */
    @Override
    public Long createUser(UserCreateRequest userCreateRequest) {
        User user = new User();
        BeanUtils.copyProperties(userCreateRequest, user);
        String password = user.getUserPassword();
        if (password == null) {
            password = UserConstant.DEFAULT_PASSWORD;
        }
        user.setUserPassword(password);
        // 校验数据
        validUser(user, true);
        // 密码加密
        user.setUserPassword(getEncryptPassword(password));
        // 操作数据库，插入数据
        boolean result = this.save(user);
        ThrowUtils.throwIf(!result, ResultCode.SYSTEM_ERROR, "创建失败，数据库异常");
        return user.getId();
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 用户更新请求体
     * @return 是否更新成功
     */
    @Override
    public Boolean updateUser(UserUpdateRequest userUpdateRequest) {
        // 封装类转对象
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        // 校验数据
        validUser(user, false);
        // 密码加密
        if (StringUtils.isNotBlank(userUpdateRequest.getUserPassword())) {
            user.setUserPassword(getEncryptPassword(userUpdateRequest.getUserPassword()));
        }
        // 操作数据库，修改数据
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ResultCode.SYSTEM_ERROR, "更新失败，数据库异常");
        return true;
    }

    /**
     * 编辑用户信息
     *
     * @param userEditRequest 用户编辑请求体
     * @return 是否编辑成功
     */
    @Override
    public Boolean editUser(UserEditRequest userEditRequest) {
        // 权限校验
        Long id = userEditRequest.getId();
        User loginUser = getLoginUser();
        ThrowUtils.throwIf(!loginUser.getId().equals(id), ResultCode.NO_AUTH_ERROR);
        // 封装类转对象
        User user = new User();
        BeanUtils.copyProperties(userEditRequest, user);
        // 校验数据
        validUser(user, false);
        // 填充默认值
        user.setEditTime(LocalDateTime.now());
        // 操作数据库，修改数据
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ResultCode.SYSTEM_ERROR, "编辑失败，数据库异常");
        return true;
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteUser(Long id) {
        // 校验数据是否存在
        User user = this.getById(id);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR);
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ResultCode.SYSTEM_ERROR, "删除失败，数据库异常");
        return true;
    }

    /**
     * 获取脱敏用户信息
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取加密密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = UserConstant.SALT;
        return DigestUtil.md5Hex(userPassword + SALT);
    }

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户查询请求体
     * @return 查询条件
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (userQueryRequest == null) {
            return queryWrapper;
        }
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userPhone = userQueryRequest.getUserPhone();
        String userEmail = userQueryRequest.getUserEmail();
        Integer userGender = userQueryRequest.getUserGender();
        LocalDate userBirthday = userQueryRequest.getUserBirthday();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(userName), "user_name", userName);
        queryWrapper.like(StringUtils.isNotBlank(userEmail), "user_email", userEmail);
        // 精准查询
        queryWrapper.eq(StringUtils.isNotBlank(userAccount), "user_account", userAccount);
        queryWrapper.eq(StringUtils.isNotBlank(userPhone), "user_phone", userPhone);
        queryWrapper.eq(userGender != null, "user_gender", userGender);
        queryWrapper.eq(userBirthday != null, "user_birthday", userBirthday);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        // 排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    /**
     * 校验用户信息
     *
     * @param user 用户信息
     * @param add  是否添加
     */
    @Override
    public void validUser(User user, boolean add) {
        ThrowUtils.throwIf(user == null, ResultCode.PARAMS_ERROR);
        // 从对象中取值
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        String phone = user.getUserPhone();
        String email = user.getUserEmail();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(userAccount), ResultCode.PARAMS_ERROR, "用户账号不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(userPassword), ResultCode.PARAMS_ERROR, "用户密码不能为空");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(userAccount)) {
            ThrowUtils.throwIf(userAccount.length() < 4 || userAccount.length() > 20,
                    ResultCode.PARAMS_ERROR, "用户账号长度必须在 4 ~ 20 位之间");
            ThrowUtils.throwIf(Validator.hasChinese(userAccount), ResultCode.PARAMS_ERROR, "用户账号不能包含中文");
            ThrowUtils.throwIf(!Validator.isGeneral(userAccount), ResultCode.PARAMS_ERROR, "用户账号不能包含特殊字符");
        }
        if (StringUtils.isNotBlank(userPassword)) {
            ThrowUtils.throwIf(userPassword.length() < 6 || userPassword.length() > 20,
                    ResultCode.PARAMS_ERROR, "用户密码长度必须在 6 ~ 20 位之间");
            ThrowUtils.throwIf(Validator.hasChinese(userPassword), ResultCode.PARAMS_ERROR, "用户密码不能包含中文");
            ThrowUtils.throwIf(!Validator.isGeneral(userPassword), ResultCode.PARAMS_ERROR, "用户密码不能包含特殊字符");
        }
        if (StringUtils.isNotBlank(phone)) {
            ThrowUtils.throwIf(!PhoneUtil.isMobile(phone), ResultCode.PARAMS_ERROR, "手机号格式错误");
        }
        if (StringUtils.isNotBlank(email)) {
            ThrowUtils.throwIf(!Validator.isEmail(email), ResultCode.PARAMS_ERROR, "邮箱格式错误");
        }
    }
}




