package com.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.model.dto.user.UserLoginRequest;
import com.lilemy.xiaoxinshu.model.dto.user.UserQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.user.UserRegisterRequest;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.model.vo.user.LoginUserVo;
import com.lilemy.xiaoxinshu.model.vo.user.UserByAdminVo;
import com.lilemy.xiaoxinshu.model.vo.user.UserVo;
import com.lilemy.xiaoxinshu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 *
 * @author lilemy
 * @date 2025-08-13 12:04
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@Validated @RequestBody UserRegisterRequest req) {
        return ResultUtils.success(userService.userRegister(req));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVo> userLogin(@Validated @RequestBody UserLoginRequest req) {
        return ResultUtils.success(userService.userLogin(req));
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(userService.userLogout());
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/login")
    public BaseResponse<LoginUserVo> getLoginUser() {
        User loginUser = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginuserVo(loginUser));
    }

    @Operation(summary = "获取用户信息（仅管理员）")
    @GetMapping("/byAdmin/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<UserByAdminVo> getUserByAdmin(@NotNull(message = "主键不能为空")
                                                      @PathVariable Long id) {
        User user = userService.getById(id);
        return ResultUtils.success(userService.getUserByAdminVo(user));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/{id}")
    public BaseResponse<UserVo> getUserVo(@NotNull(message = "主键不能为空")
                                          @PathVariable Long id) {
        return ResultUtils.success(userService.getUserVo(id));
    }

    @Operation(summary = "分页获取用户信息（仅管理员）")
    @GetMapping("/byAdmin/page")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Page<UserByAdminVo>> listUserByAdminPage(UserQueryRequest req, PageQuery pageQuery) {
        return ResultUtils.success(userService.getUserByAdminVoPage(req, pageQuery));
    }
}