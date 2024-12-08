package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import cn.lilemy.xiaoxinshu.service.UserInterfaceInfoService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/userInterfaceInfo")
@Tag(name = "UserInterfaceInfoController")
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Operation(summary = "添加用户接口关系")
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest addRequest) {
        ThrowUtils.throwIf(addRequest == null, ResultCode.PARAMS_ERROR);
        Long userInterfaceInfoId = userInterfaceInfoService.addUserInterfaceInfo(addRequest);
        return ResultUtils.success(userInterfaceInfoId);
    }

    @Operation(summary = "更新用户接口调用次数")
    @PostMapping("/update/leftNum")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfoLeftNum(@RequestBody UserInterfaceInfoUpdateRequest updateRequest) {
        ThrowUtils.throwIf(updateRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = userInterfaceInfoService.updateUserInterfaceInfoLeftNum(updateRequest);
        return ResultUtils.success(result);
    }
}
