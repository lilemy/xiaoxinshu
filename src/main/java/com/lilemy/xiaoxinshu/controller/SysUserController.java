package com.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.annotation.RepeatSubmit;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.model.dto.user.*;
import com.lilemy.xiaoxinshu.model.entity.SysUser;
import com.lilemy.xiaoxinshu.model.vo.user.SysLoginUserVo;
import com.lilemy.xiaoxinshu.model.vo.user.SysUserByAdminVo;
import com.lilemy.xiaoxinshu.model.vo.user.SysUserVo;
import com.lilemy.xiaoxinshu.service.SysUserService;
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
@RequestMapping("/sys/user")
@Tag(name = "SysUserController", description = "用户接口")
public class SysUserController {

    @Resource
    private SysUserService userService;

    @Operation(summary = "用户注册")
    @RepeatSubmit()
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@Validated @RequestBody SysUserRegisterRequest req) {
        return ResultUtils.success(userService.userRegister(req));
    }

    @Operation(summary = "用户登录")
    @RepeatSubmit()
    @PostMapping("/login")
    public BaseResponse<SysLoginUserVo> userLogin(@Validated @RequestBody SysUserLoginRequest req) {
        return ResultUtils.success(userService.userLogin(req));
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(userService.userLogout());
    }

    @Operation(summary = "创建用户信息（仅管理员）")
    @RepeatSubmit()
    @PostMapping("/create")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Long> createUser(@Validated @RequestBody SysUserCreateRequest req) {
        return ResultUtils.success(userService.createUser(req));
    }

    @Operation(summary = "更新用户信息（仅管理员）")
    @RepeatSubmit()
    @PutMapping("/update")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> updateUser(@Validated @RequestBody SysUserUpdateRequest req) {
        return ResultUtils.success(userService.updateUser(req));
    }

    @Operation(summary = "删除用户信息（仅管理员）")
    @DeleteMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> deleteUser(@NotNull(message = "主键不能为空")
                                            @PathVariable Long id) {
        return ResultUtils.success(userService.deleteUser(id));
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/login")
    public BaseResponse<SysLoginUserVo> getLoginUser() {
        SysUser loginUser = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginUserVo(loginUser));
    }

    @Operation(summary = "获取用户信息（仅管理员）")
    @GetMapping("/byAdmin/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<SysUserByAdminVo> getUserByAdmin(@NotNull(message = "主键不能为空")
                                                         @PathVariable Long id) {
        SysUser user = userService.getById(id);
        return ResultUtils.success(userService.getUserByAdminVo(user));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/{id}")
    public BaseResponse<SysUserVo> getUserVo(@NotNull(message = "主键不能为空")
                                             @PathVariable Long id) {
        return ResultUtils.success(userService.getUserVo(id));
    }

    @Operation(summary = "分页获取用户信息（仅管理员）")
    @GetMapping("/byAdmin/page")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Page<SysUserByAdminVo>> listUserByAdminPage(SysUserQueryRequest req, PageQuery pageQuery) {
        return ResultUtils.success(userService.getUserByAdminVoPage(req, pageQuery));
    }
}