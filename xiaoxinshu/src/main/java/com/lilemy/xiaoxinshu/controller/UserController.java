package com.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.common.*;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.model.dto.user.*;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.model.vo.user.LoginUserVO;
import com.lilemy.xiaoxinshu.model.vo.user.UserVO;
import com.lilemy.xiaoxinshu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author lilemy
 * @date 2025/06/03 11:21
 */
@RestController
@Tag(name = "UserController", description = "用户接口")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        ThrowUtils.throwIf(userLoginRequest == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.userLogin(userLoginRequest));
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(userService.userLogout());
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/login")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User loginUser = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @Operation(summary = "获取用户信息（仅管理员）")
    @GetMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<User> getUser(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.getById(id));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/vo/{id}")
    public BaseResponse<UserVO> getUserVO(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.getUserVO(id));
    }

    @Operation(summary = "创建用户（仅管理员）")
    @PostMapping()
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Long> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        ThrowUtils.throwIf(userCreateRequest == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.createUser(userCreateRequest));
    }

    @Operation(summary = "更新用户信息")
    @PutMapping()
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.updateUser(userUpdateRequest));
    }

    @Operation(summary = "用户编辑个人信息")
    @PutMapping("/edit")
    public BaseResponse<Boolean> editUser(@RequestBody UserEditRequest userEditRequest) {
        ThrowUtils.throwIf(userEditRequest == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.editUser(userEditRequest));
    }

    @Operation(summary = "删除用户（仅管理员）")
    @DeleteMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(userService.deleteUser(id));
    }

    @Operation(summary = "分页获取用户信息（仅管理员）")
    @GetMapping("/page")
    @SaCheckRole(UserConstant.ADMIN)
    public TableDataInfo<User> listUserByPage(UserQueryRequest userQueryRequest, PageQuery pageQuery) {
        ThrowUtils.throwIf(userQueryRequest == null, ResultCode.PARAMS_ERROR);
        Page<User> result = userService.page(pageQuery.build(), userService.getQueryWrapper(userQueryRequest));
        return TableDataInfo.build(result);
    }
}
