package com.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.lilemy.xiaoxinshu.annotation.RepeatSubmit;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.manager.rustfs.OssHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 对象存储接口
 *
 * @author lilemy
 * @date 2026-01-08 17:05
 */
@Slf4j
@RestController
@RequestMapping("/sys/oss")
@Tag(name = "SysOssController", description = "对象存储接口")
public class SysOssController {

    @Resource
    private OssHelper ossHelper;

    @Operation(summary = "文件上传")
    @RepeatSubmit()
    @PostMapping("/upload")
    @SaCheckRole(value = {UserConstant.ADMIN, UserConstant.USER}, mode = SaMode.OR)
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file, String prefix) {
        return ResultUtils.success(ossHelper.uploadFile(prefix, file));
    }
}
