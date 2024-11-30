package cn.lilemy.xiaoxinshu.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lilemy.xiaoxinshu.common.ResultCode;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.constant.RedisConstant;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.exception.BusinessException;
import cn.lilemy.xiaoxinshu.exception.ThrowUtils;
import cn.lilemy.xiaoxinshu.mapper.UserMapper;
import cn.lilemy.xiaoxinshu.model.dto.user.UserEditRequest;
import cn.lilemy.xiaoxinshu.model.dto.user.UserQueryRequest;
import cn.lilemy.xiaoxinshu.model.entity.User;
import cn.lilemy.xiaoxinshu.model.vo.LoginUserVO;
import cn.lilemy.xiaoxinshu.model.vo.UserVO;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshu.util.DeviceUtils;
import cn.lilemy.xiaoxinshu.util.SqlUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qq233
 * @description 针对表【user(用户)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账户过短");
        }
        if (userAccount.length() > 20) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账户太长");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户密码过短");
        }
        if (userPassword.length() > 40 || checkPassword.length() > 40) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户密码太长");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserAccount, userAccount);
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "账号重复");
            }
            // 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((CommonConstant.SALT + userPassword).getBytes());
            String accessKey = DigestUtils.md5DigestAsHex((CommonConstant.SALT + userAccount).getBytes());
            String secretKey = DigestUtils.md5DigestAsHex((CommonConstant.SALT + userAccount + System.currentTimeMillis()).getBytes());
            // 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            // 默认名称+当前时间戳
            user.setUsername(UserConstant.DEFAULT_NICKNAME + System.currentTimeMillis());
            // 默认头像
            user.setUserAvatar(UserConstant.DEFAULT_AVATAR);
            boolean save = this.save(user);
            ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "注册失败，数据库错误");
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 参数校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账户过短");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户密码过短");
        }
        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((CommonConstant.SALT + userPassword).getBytes());
        // 查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        User user = this.getOne(queryWrapper);
        // 用户不存在
        ThrowUtils.throwIf(user == null, ResultCode.PARAMS_ERROR, "用户不存在");
        // 密码是否匹配
        ThrowUtils.throwIf(!encryptPassword.equals(user.getUserPassword()), ResultCode.PARAMS_ERROR, "密码错误");
        // 判断用户是否被封号
        ThrowUtils.throwIf(user.getUserRole().equals(UserConstant.BAN_ROLE), ResultCode.NO_AUTH_ERROR, "您已被封号，请联系管理员！");
        // 记录用户的登录态，指定设备，同端登录互斥
        StpUtil.login(user.getId(), DeviceUtils.getRequestDevice(request));
        StpUtil.getTokenSession().set(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public boolean userEdit(UserEditRequest userEditRequest) {
        Long id = userEditRequest.getId();
        String username = userEditRequest.getUsername();
        String userAvatar = userEditRequest.getUserAvatar();
        String userProfile = userEditRequest.getUserProfile();
        String phone = userEditRequest.getPhone();
        String email = userEditRequest.getEmail();
        User loginUser = this.getLoginUser();
        ThrowUtils.throwIf(!id.equals(loginUser.getId()), ResultCode.NO_AUTH_ERROR);
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setUserAvatar(userAvatar);
        user.setUserProfile(userProfile);
        user.setPhone(phone);
        user.setEmail(email);
        user.setEditTime(new Date());
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public boolean userLogout() {
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
        User currentUser = (User) userObj;
        // 从数据库查询
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ResultCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        ThrowUtils.throwIf(user == null, ResultCode.PARAMS_ERROR);
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(user.getId());
        loginUserVO.setUsername(user.getUsername());
        loginUserVO.setUserAccount(user.getUserAccount());
        loginUserVO.setUserAvatar(user.getUserAvatar());
        loginUserVO.setUserProfile(user.getUserProfile());
        loginUserVO.setPhone(user.getPhone());
        loginUserVO.setEmail(user.getEmail());
        loginUserVO.setUserRole(user.getUserRole());
        loginUserVO.setEditTime(user.getEditTime());
        loginUserVO.setUpdateTime(user.getUpdateTime());
        loginUserVO.setCreateTime(user.getCreateTime());
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getUserVO(Long userId) {
        User user = null;
        if (userId != null && userId > 0) {
            user = this.getById(userId);
        }
        return this.getUserVO(user);
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public boolean isAdmin() {
        User loginUser = getLoginUser();
        return isAdmin(loginUser);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }

    @Override
    public boolean addUserSignIn(long userId) {
        LocalDate date = LocalDate.now();
        String key = RedisConstant.getUserSignInRedisKey(date.getYear(), userId);
        RBitSet signInBitSet = redissonClient.getBitSet(key);
        // 获取当前日期是一年中的第几天，作为偏移量（从 1 开始计数）
        int offset = date.getDayOfYear();
        // 检查当天是否已经签到
        if (!signInBitSet.get(offset)) {
            // 如果当前还未签到，则设置签到
            try {
                signInBitSet.set(offset, true);
            } catch (Exception e) {
                throw new BusinessException(ResultCode.OPERATION_ERROR);
            }
            return true;
        }
        // 当天已签到
        return true;
    }

    @Override
    public List<Integer> getUserSignInRecord(long userId, Integer year) {
        if (year == null) {
            LocalDate date = LocalDate.now();
            year = date.getYear();
        }
        String key = RedisConstant.getUserSignInRedisKey(year, userId);
        RBitSet signInBitSet = redissonClient.getBitSet(key);
        // 加载 BitSet 到内存中，避免后续读取时发送多次请求
        BitSet bitSet = signInBitSet.asBitSet();
        // 统计签到的日期
        List<Integer> dayList = new ArrayList<>();
        // 获取当前年份的总天数
        int totalDays = Year.of(year).length();
        // 依次获取每一天的签到状态
        for (int dayOfYear = 1; dayOfYear <= totalDays; dayOfYear++) {
            // 获取 value：当天是否有刷题
            boolean hasRecord = bitSet.get(dayOfYear);
            if (hasRecord) {
                dayList.add(dayOfYear);
            }
        }
        return dayList;
    }

    @Override
    public Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ResultCode.PARAMS_ERROR);
        Long id = userQueryRequest.getId();
        String username = userQueryRequest.getUsername();
        String userAccount = userQueryRequest.getUserAccount();
        String userRole = userQueryRequest.getUserRole();
        String phone = userQueryRequest.getPhone();
        String email = userQueryRequest.getEmail();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.eq(StringUtils.isNotBlank(userAccount), "user_account", userAccount);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.like(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.like(StringUtils.isNotBlank(email), "email", email);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }
}




