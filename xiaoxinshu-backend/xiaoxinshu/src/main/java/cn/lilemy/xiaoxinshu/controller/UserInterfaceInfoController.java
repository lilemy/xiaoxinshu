package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import cn.lilemy.xiaoxinshu.service.UserInterfaceInfoService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Operation(summary = "删除用户接口关系")
    @PostMapping("/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(userInterfaceInfo == null, ResultCode.PARAMS_ERROR);
        boolean result = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新用户接口调用次数")
    @PostMapping("/update/leftNum")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfoLeftNum(@RequestBody UserInterfaceInfoUpdateRequest updateRequest) {
        ThrowUtils.throwIf(updateRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = userInterfaceInfoService.updateUserInterfaceInfoLeftNum(updateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "分页查询用户接口关系")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest queryRequest) {
        ThrowUtils.throwIf(queryRequest == null, ResultCode.PARAMS_ERROR);
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        // 查询数据库
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(queryRequest));
        return ResultUtils.success(userInterfaceInfoPage);
    }
}
