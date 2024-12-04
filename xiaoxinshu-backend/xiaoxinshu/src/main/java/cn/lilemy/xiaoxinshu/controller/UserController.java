package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.user.*;
import cn.lilemy.xiaoxinshu.model.entity.User;
import cn.lilemy.xiaoxinshu.model.vo.LoginUserVO;
import cn.lilemy.xiaoxinshu.model.vo.UserVO;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "UserController")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        boolean result = userService.userLogout();
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User user = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 用户增删改

    @Operation(summary = "创建用户")
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ResultCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 123456
        String defaultPassword = UserConstant.DEFAULT_PASSWORD;
        String encryptPassword = DigestUtils.md5DigestAsHex((CommonConstant.SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @Operation(summary = "更新用户")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null || userUpdateRequest.getId() == null, ResultCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "用户编辑")
    @PostMapping("/edit")
    public BaseResponse<Boolean> userEdit(@RequestBody UserEditRequest userEditRequest) {
        ThrowUtils.throwIf(userEditRequest == null || userEditRequest.getId() == null, ResultCode.PARAMS_ERROR);
        boolean result = userService.userEdit(userEditRequest);
        return ResultUtils.success(result);
    }

    // endregion

    // region 用户查询

    @Operation(summary = "根据 id 获取用户（仅管理员）")
    @GetMapping("/get")
    public BaseResponse<User> getUserById(Long id) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @Operation(summary = "根据 id 获取脱敏用户信息")
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        User user = userService.getById(id);
        return ResultUtils.success(userService.getUserVO(user));
    }

    @Operation(summary = "分页获取用户列表")
    @PostMapping("/list")
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    @Operation(summary = "分页获取用户封装列表")
    @PostMapping("/list/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    // region 用户签到

    @Operation(summary = "添加用户签到记录")
    @PostMapping("/add/sign_in")
    public BaseResponse<Boolean> addUserSignIn() {
        // 必须要登录才能签到
        User loginUser = userService.getLoginUser();
        boolean result = userService.addUserSignIn(loginUser.getId());
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取用户签到记录")
    @GetMapping("/get/sign_in")
    public BaseResponse<List<Integer>> getUserSignInRecord(Integer year) {
        // 必须要登录才能获取
        User loginUser = userService.getLoginUser();
        List<Integer> userSignInRecord = userService.getUserSignInRecord(loginUser.getId(), year);
        return ResultUtils.success(userSignInRecord);
    }

    // endregion

}
