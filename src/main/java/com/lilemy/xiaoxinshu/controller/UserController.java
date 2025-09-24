package com.lilemy.xiaoxinshu.controller;

import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public BaseResponse<User> getInfo(Long id) {
        return ResultUtils.success(userService.getById(id));
    }
}
