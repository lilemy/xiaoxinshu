package com.lilemy.xiaoxinshu.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.mapper.UserMapper;
import com.lilemy.xiaoxinshu.model.dto.user.UserLoginRequest;
import com.lilemy.xiaoxinshu.model.dto.user.UserQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.user.UserRegisterRequest;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.model.enums.UserRoleEnum;
import com.lilemy.xiaoxinshu.model.vo.user.LoginUserVo;
import com.lilemy.xiaoxinshu.model.vo.user.UserByAdminVo;
import com.lilemy.xiaoxinshu.model.vo.user.UserVo;
import com.lilemy.xiaoxinshu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户服务实现
 *
 * @author lilemy
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-08-13 11:34:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public Long userRegister(UserRegisterRequest req) {
        // 1.参数校验
        String userAccount = req.getUserAccount();
        String userPassword = req.getUserPassword();
        String checkPassword = req.getCheckPassword();
        ThrowUtils.throwIf(userAccount.length() < 4, ResultCode.PARAMS_ERROR, "用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < 8 || checkPassword.length() < 8, ResultCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ResultCode.PARAMS_ERROR, "两次输入的密码不一致");
        // 2.检查是否重复
        Long count = this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .count();
        ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "账号重复");
        // 3.加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4.拼接默认用户名
        String defaultUserName = UserConstant.DEFAULT_NICKNAME + "-" + RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz0123456789", 8);
        // 5.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(defaultUserName);
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "注册失败，数据库异常");
        return user.getId();
    }

    @Override
    public LoginUserVo userLogin(UserLoginRequest req) {
        // 1.参数校验
        String userAccount = req.getUserAccount();
        String userPassword = req.getUserPassword();
        ThrowUtils.throwIf(userAccount.length() < 4, ResultCode.PARAMS_ERROR, "用户账号错误");
        ThrowUtils.throwIf(userPassword.length() < 8, ResultCode.PARAMS_ERROR, "用户密码错误");
        // 2.查询用户
        User user = this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .last("limit 1")
                .one();
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR, "用户不存在");
        // 3.校验密码
        ThrowUtils.throwIf(!user.getUserPassword().equals(getEncryptPassword(userPassword)), ResultCode.PARAMS_ERROR, "密码错误");
        // 4.判断用户是否被封号
        ThrowUtils.throwIf(UserRoleEnum.BAN.getValue().equals(user.getUserRole()), ResultCode.NO_AUTH_ERROR, "您已被封号，请联系管理员！");
        // 5.记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getTokenSession().set(UserConstant.USER_LOGIN_STATE, user);
        // 6.返回用户信息
        return getLoginuserVo(user);
    }

    @Override
    public Boolean userLogout() {
        StpUtil.checkLogin();
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    @Override
    public User getLoginUser() {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ResultCode.NOT_LOGIN_ERROR);
        // 先判断是否已登录
        Object userObj = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        // 从数据库查询
        long userId = user.getId();
        user = this.getById(userId);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_LOGIN_ERROR);
        return user;
    }

    @Override
    public UserVo getUserVo(User user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        // 隐藏手机号
        String userPhone = user.getUserPhone();
        if (StringUtils.isNotBlank(userPhone)) {
            userVo.setUserPhone(PhoneUtil.hideBetween(userPhone).toString());
        }
        // 隐藏邮箱
        String userEmail = user.getUserEmail();
        if (StringUtils.isNotBlank(userEmail)) {
            userVo.setUserEmail(DesensitizedUtil.email(userEmail));
        }
        return userVo;
    }

    @Override
    public UserVo getUserVo(Long userId) {
        User user = null;
        if (userId != null && userId > 0) {
            user = this.getById(userId);
        }
        return this.getUserVo(user);
    }

    @Override
    public LoginUserVo getLoginuserVo(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        return loginUserVo;
    }

    @Override
    public UserByAdminVo getUserByAdminVo(User user) {
        if (user == null) {
            return null;
        }
        UserByAdminVo userByAdminVo = new UserByAdminVo();
        BeanUtils.copyProperties(user, userByAdminVo);
        return userByAdminVo;
    }

    @Override
    public Page<UserByAdminVo> getUserByAdminVoPage(UserQueryRequest req, PageQuery pageQuery) {
        Page<User> userPage = this.page(pageQuery.build(), this.getQueryWrapper(req));
        List<User> userList = userPage.getRecords();
        Page<UserByAdminVo> userByAdminVoPage = new Page<>(
                userPage.getCurrent(),
                userPage.getSize(),
                userPage.getTotal());
        if (CollUtil.isEmpty(userList)) {
            return userByAdminVoPage;
        }
        // 填充信息
        List<UserByAdminVo> userByAdminVoList = userList.stream().map(user -> {
            UserByAdminVo userByAdminVo = new UserByAdminVo();
            BeanUtils.copyProperties(user, userByAdminVo);
            return userByAdminVo;
        }).toList();
        userByAdminVoPage.setRecords(userByAdminVoList);
        return userByAdminVoPage;
    }

    @Override
    public Boolean isAdmin() {
        User loginUser = getLoginUser();
        return isAdmin(loginUser);
    }

    @Override
    public Boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest req) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        if (req == null) {
            return lqw;
        }
        String userAccount = req.getUserAccount();
        String userName = req.getUserName();
        String userPhone = req.getUserPhone();
        String userEmail = req.getUserEmail();
        Integer userGender = req.getUserGender();
        LocalDate userBirthday = req.getUserBirthday();
        Integer userRole = req.getUserRole();
        // 模糊查询
        lqw.like(StringUtils.isNotBlank(userName), User::getUserName, userName);
        lqw.like(StringUtils.isNotBlank(userEmail), User::getUserEmail, userEmail);
        // 精准查询
        lqw.eq(StringUtils.isNotBlank(userAccount), User::getUserAccount, userAccount);
        lqw.eq(StringUtils.isNotBlank(userPhone), User::getUserPhone, userPhone);
        lqw.eq(userGender != null, User::getUserGender, userGender);
        lqw.eq(userBirthday != null, User::getUserBirthday, userBirthday);
        lqw.eq(userRole != null, User::getUserRole, userRole);
        return lqw;
    }

    /**
     * 获取加密密码
     *
     * @param userPassword 待加密密码
     * @return 加密后的密码
     */
    private String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "lilemy";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

}




