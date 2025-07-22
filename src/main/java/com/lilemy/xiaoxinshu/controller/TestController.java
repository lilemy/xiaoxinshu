package com.lilemy.xiaoxinshu.controller;

import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author lilemy
 * @date 2025-07-20 23:24
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口")
public class TestController {

    @Operation(summary = "获取用户信息")
    @PostMapping("/userInfo")
    public BaseResponse<User> getUserInfo(@RequestBody @Validated User user) {
        // 设置三种日期字段值
        user.setDateTime(LocalDateTime.now());
        user.setDate(LocalDate.now());
        user.setTime(LocalTime.now());
        return ResultUtils.success(user);
    }

}
