package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.space.*;
import cn.lilemy.xiaoxinshu.model.enums.SpaceLevelEnum;
import cn.lilemy.xiaoxinshu.service.SpaceService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.Space;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/space")
@Tag(name = "SpaceController")
public class SpaceController {

    @Resource
    private SpaceService spaceService;

    @Operation(summary = "创建空间")
    @PostMapping("/create")
    public BaseResponse<Long> createSpace(@RequestBody SpaceCreateRequest spaceCreateRequest) {
        ThrowUtils.throwIf(spaceCreateRequest == null, ResultCode.PARAMS_ERROR);
        Long spaceId = spaceService.createSpace(spaceCreateRequest);
        return ResultUtils.success(spaceId);
    }

    @Operation(summary = "更新空间（仅管理员）")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpace(@RequestBody SpaceUpdateRequest spaceUpdateRequest) {
        ThrowUtils.throwIf(spaceUpdateRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = spaceService.updateSpace(spaceUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "编辑空间")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpace(@RequestBody SpaceEditRequest spaceEditRequest) {
        ThrowUtils.throwIf( spaceEditRequest==null||spaceEditRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        Boolean result = spaceService.editSpace(spaceEditRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "分页获取空间列表（仅管理员）")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Space>> listPictureByPage(@RequestBody SpaceQueryRequest spaceQueryRequest) {
        int current = spaceQueryRequest.getCurrent();
        int pageSize = spaceQueryRequest.getPageSize();
        // 查询数据库
        Page<Space> spacePage = spaceService.page(new Page<>(current, pageSize),
                spaceService.getQueryWrapper(spaceQueryRequest));
        return ResultUtils.success(spacePage);
    }

    @Operation(summary = "获取空间等级列表")
    @GetMapping("/list/level")
    public BaseResponse<List<SpaceLevel>> listSpaceLevel() {
        // 获取所有枚举
        List<SpaceLevel> spaceLevelList = Arrays.stream(SpaceLevelEnum.values())
                .map(spaceLevelEnum -> new SpaceLevel(
                        spaceLevelEnum.getValue(),
                        spaceLevelEnum.getText(),
                        spaceLevelEnum.getMaxCount(),
                        spaceLevelEnum.getMaxSize()))
                .collect(Collectors.toList());
        return ResultUtils.success(spaceLevelList);
    }

}
