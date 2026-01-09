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
import com.lilemy.xiaoxinshu.manager.rustfs.OssHelper;
import com.lilemy.xiaoxinshu.mapper.SysUserMapper;
import com.lilemy.xiaoxinshu.model.dto.user.*;
import com.lilemy.xiaoxinshu.model.entity.SysUser;
import com.lilemy.xiaoxinshu.model.enums.SysUserRoleEnum;
import com.lilemy.xiaoxinshu.model.vo.user.SysLoginUserVo;
import com.lilemy.xiaoxinshu.model.vo.user.SysUserByAdminVo;
import com.lilemy.xiaoxinshu.model.vo.user.SysUserVo;
import com.lilemy.xiaoxinshu.service.SysUserService;
import jakarta.annotation.Resource;
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
 * @description 针对表【sys_user(用户)】的数据库操作Service实现
 * @createDate 2025-08-13 11:34:58
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Resource
    private OssHelper ossHelper;

    @Override
    public Long userRegister(SysUserRegisterRequest req) {
        // 1.参数校验
        String userAccount = req.getUserAccount();
        String userPassword = req.getUserPassword();
        String checkPassword = req.getCheckPassword();
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ResultCode.PARAMS_ERROR, "两次输入的密码不一致");
        // 2.检查是否重复
        Long count = this.lambdaQuery()
                .eq(SysUser::getUserAccount, userAccount)
                .count();
        ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "账号重复");
        // 3.加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4.拼接默认用户名
        String defaultUserName = UserConstant.DEFAULT_NICKNAME + "-" + RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz0123456789", 8);
        // 5.插入数据
        SysUser user = new SysUser();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(defaultUserName);
        user.setUserRole(SysUserRoleEnum.USER.getValue());
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "注册失败，数据库异常");
        return user.getId();
    }

    @Override
    public SysLoginUserVo userLogin(SysUserLoginRequest req) {
        // 1.参数校验
        String userAccount = req.getUserAccount();
        String userPassword = req.getUserPassword();
        // 2.查询用户
        SysUser user = this.lambdaQuery()
                .eq(SysUser::getUserAccount, userAccount)
                .last("limit 1")
                .one();
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR, "用户不存在");
        // 3.校验密码
        ThrowUtils.throwIf(!user.getUserPassword().equals(getEncryptPassword(userPassword)), ResultCode.PARAMS_ERROR, "密码错误");
        // 4.判断用户是否被封号
        ThrowUtils.throwIf(SysUserRoleEnum.BAN.getValue().equals(user.getUserRole()), ResultCode.NO_AUTH_ERROR, "您已被封号，请联系管理员！");
        // 5.记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getTokenSession().set(UserConstant.USER_LOGIN_STATE, user);
        // 6.返回用户信息
        return getLoginUserVo(user);
    }

    @Override
    public Boolean userLogout() {
        StpUtil.checkLogin();
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    @Override
    public Long createUser(SysUserCreateRequest req) {
        // 1.将请求体转换为实体
        SysUser user = new SysUser();
        BeanUtils.copyProperties(req, user);
        // 2.参数校验
        Long count = this.lambdaQuery()
                .eq(SysUser::getUserAccount, user.getUserAccount())
                .count();
        ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "账号重复");
        // 3.填充默认用户名
        if (StringUtils.isBlank(user.getUserName())) {
            String defaultUserName = UserConstant.DEFAULT_NICKNAME + "-" + RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz0123456789", 8);
            user.setUserName(defaultUserName);
        }
        // 4.设置默认密码
        String encryptPassword = getEncryptPassword(UserConstant.DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        // 5.插入数据
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "创建用户失败，数据库异常");
        // 6.返回用户id
        return user.getId();
    }

    @Override
    public Boolean updateUser(SysUserUpdateRequest req) {
        // 1.将请求体转换为实体
        SysUser user = new SysUser();
        BeanUtils.copyProperties(req, user);
        // 2.校验数据是否存在
        SysUser oldUser = this.getById(user.getId());
        // 3.参数校验
        String userAccount = req.getUserAccount();
        if (StringUtils.isNotBlank(userAccount) && !userAccount.equals(oldUser.getUserAccount())) {
            Long count = this.lambdaQuery()
                    .eq(SysUser::getUserAccount, userAccount)
                    .ne(SysUser::getId, user.getId())
                    .count();
            ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "账号重复");
        }
        // 校验图片，如果图片改变，删除旧图片
        if (StringUtils.isNotBlank(req.getUserAvatar())&& !req.getUserAvatar().equals(oldUser.getUserAvatar())) {
            ossHelper.deleteFileByUrl(oldUser.getUserAvatar());
        }
        // 4.更新数据
        boolean update = this.updateById(user);
        ThrowUtils.throwIf(!update, ResultCode.SYSTEM_ERROR, "更新用户失败，数据库异常");
        return true;
    }

    @Override
    public Boolean deleteUser(Long id) {
        // 1.校验数据是否存在
        SysUser user = this.getById(id);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR);
        // 2.删除数据
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ResultCode.SYSTEM_ERROR, "删除用户失败，数据库异常");
        return true;
    }

    @Override
    public SysUser getLoginUser() {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ResultCode.NOT_LOGIN_ERROR);
        // 先判断是否已登录
        Object userObj = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        SysUser user = (SysUser) userObj;
        // 从数据库查询
        long userId = user.getId();
        user = this.getById(userId);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_LOGIN_ERROR);
        return user;
    }

    @Override
    public SysUserVo getUserVo(SysUser user) {
        if (user == null) {
            return null;
        }
        SysUserVo userVo = new SysUserVo();
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
    public SysUserVo getUserVo(Long userId) {
        SysUser user = null;
        if (userId != null && userId > 0) {
            user = this.getById(userId);
        }
        return this.getUserVo(user);
    }

    @Override
    public SysLoginUserVo getLoginUserVo(SysUser user) {
        if (user == null) {
            return null;
        }
        SysLoginUserVo loginUserVo = new SysLoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        return loginUserVo;
    }

    @Override
    public SysUserByAdminVo getUserByAdminVo(SysUser user) {
        if (user == null) {
            return null;
        }
        SysUserByAdminVo userByAdminVo = new SysUserByAdminVo();
        BeanUtils.copyProperties(user, userByAdminVo);
        return userByAdminVo;
    }

    @Override
    public Page<SysUserByAdminVo> getUserByAdminVoPage(SysUserQueryRequest req, PageQuery pageQuery) {
        Page<SysUser> userPage = this.page(pageQuery.build(), this.getQueryWrapper(req));
        List<SysUser> userList = userPage.getRecords();
        Page<SysUserByAdminVo> userByAdminVoPage = new Page<>(
                userPage.getCurrent(),
                userPage.getSize(),
                userPage.getTotal());
        if (CollUtil.isEmpty(userList)) {
            return userByAdminVoPage;
        }
        // 填充信息
        List<SysUserByAdminVo> userByAdminVoList = userList.stream().map(user -> {
            SysUserByAdminVo userByAdminVo = new SysUserByAdminVo();
            BeanUtils.copyProperties(user, userByAdminVo);
            return userByAdminVo;
        }).toList();
        userByAdminVoPage.setRecords(userByAdminVoList);
        return userByAdminVoPage;
    }

    @Override
    public Boolean isAdmin() {
        SysUser loginUser = getLoginUser();
        return isAdmin(loginUser);
    }

    @Override
    public Boolean isAdmin(SysUser user) {
        return user != null && SysUserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public LambdaQueryWrapper<SysUser> getQueryWrapper(SysUserQueryRequest req) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
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
        lqw.like(StringUtils.isNotBlank(userName), SysUser::getUserName, userName);
        lqw.like(StringUtils.isNotBlank(userEmail), SysUser::getUserEmail, userEmail);
        // 精准查询
        lqw.eq(StringUtils.isNotBlank(userAccount), SysUser::getUserAccount, userAccount);
        lqw.eq(StringUtils.isNotBlank(userPhone), SysUser::getUserPhone, userPhone);
        lqw.eq(userGender != null, SysUser::getUserGender, userGender);
        lqw.eq(userBirthday != null, SysUser::getUserBirthday, userBirthday);
        lqw.eq(userRole != null, SysUser::getUserRole, userRole);
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




