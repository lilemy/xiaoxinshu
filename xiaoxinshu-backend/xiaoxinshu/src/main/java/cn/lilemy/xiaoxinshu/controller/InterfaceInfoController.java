package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.*;
import cn.lilemy.xiaoxinshu.model.vo.InterfaceInfoByName;
import cn.lilemy.xiaoxinshu.model.vo.InterfaceInfoVO;
import cn.lilemy.xiaoxinshu.service.InterfaceInfoService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 接口信息接口
 */
@Slf4j
@RestController
@RequestMapping("/interfaceInfo")
@Tag(name = "InterfaceInfoController")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;


    // region 增删改查

    @Operation(summary = "添加接口信息")
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest addRequest) {
        ThrowUtils.throwIf(addRequest == null, ResultCode.PARAMS_ERROR);
        Long interfaceInfoId = interfaceInfoService.addInterfaceInfo(addRequest);
        return ResultUtils.success(interfaceInfoId);
    }

    @Operation(summary = "删除接口信息")
    @PostMapping("/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        // 查看接口信息是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        // 删除接口信息
        boolean result = interfaceInfoService.removeById(id);
        ThrowUtils.throwIf(!result, ResultCode.SYSTEM_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "修改接口信息")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest updateRequest) {
        ThrowUtils.throwIf(updateRequest == null, ResultCode.PARAMS_ERROR);
        boolean result = interfaceInfoService.updateInterfaceInfo(updateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据 id 查询接口信息")
    @GetMapping("/get")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<InterfaceInfo> getInterfaceInfo(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        return ResultUtils.success(interfaceInfo);
    }

    @Operation(summary = "根据 id 查询接口信息封装")
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVO(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo));
    }

    @Operation(summary = "分页获取接口信息")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest queryRequest) {
        ThrowUtils.throwIf(queryRequest == null, ResultCode.PARAMS_ERROR);
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        QueryWrapper<InterfaceInfo> queryWrapper = interfaceInfoService.getQueryWrapper(queryRequest);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    @Operation(summary = "分页获取接口信息封装")
    @PostMapping("/list/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest queryRequest) {
        ThrowUtils.throwIf(queryRequest == null, ResultCode.PARAMS_ERROR);
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInfo> queryWrapper = interfaceInfoService.getQueryWrapper(queryRequest);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage));
    }

    @Operation(summary = "获取接口名字列表")
    @GetMapping("/get/list")
    public BaseResponse<List<InterfaceInfoByName>> listInterfaceInfoByName() {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "description");
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        List<InterfaceInfoByName> list = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoByName interfaceInfoByName = new InterfaceInfoByName();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoByName);
            return interfaceInfoByName;
        }).toList();
        return ResultUtils.success(list);
    }

    // endregion

    // region 上线下线接口
    @Operation(summary = "上线接口")
    @PostMapping("/online")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterface(@RequestBody InterfaceOnlineRequest interfaceOnlineRequest) {
        Long id = interfaceOnlineRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR, "接口不存在");
        Boolean result = interfaceInfoService.onlineInterface(id);
        return ResultUtils.success(result);
    }

    @Operation(summary = "下线接口")
    @PostMapping("/offline")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterface(@RequestBody InterfaceOfflineRequest interfaceOfflineRequest) {
        Long id = interfaceOfflineRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR, "接口不存在");
        Boolean result = interfaceInfoService.offlineInterface(id);
        return ResultUtils.success(result);
    }
    // endregion

    @Operation(summary = "在线调用接口")
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest invokeRequest) {
        ThrowUtils.throwIf(invokeRequest == null || invokeRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        Object result = interfaceInfoService.invokeInterface(invokeRequest);
        return ResultUtils.success(result);
    }
}
