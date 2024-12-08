package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.categories.CategoriesCreateRequest;
import cn.lilemy.xiaoxinshu.model.dto.categories.CategoriesQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.categories.CategoriesUpdateRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.Categories;
import cn.lilemy.xiaoxinshu.model.vo.CategoriesVO;
import cn.lilemy.xiaoxinshu.service.CategoriesService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 笔记分类接口
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@Tag(name = "CategoriesController")
public class CategoriesController {

    @Resource
    private CategoriesService categoriesService;

    @Operation(summary = "创建笔记分类")
    @PostMapping("/create")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> createCategories(@RequestBody CategoriesCreateRequest categoriesCreateRequest) {
        ThrowUtils.throwIf(categoriesCreateRequest == null, ResultCode.PARAMS_ERROR);
        Categories categories = new Categories();
        BeanUtils.copyProperties(categoriesCreateRequest, categories);
        categoriesService.validCategories(categories, true);
        boolean save = categoriesService.save(categories);
        ThrowUtils.throwIf(!save, ResultCode.SUCCESS);
        return ResultUtils.success(categories.getId());
    }

    @Operation(summary = "删除笔记分类")
    @PostMapping("/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteCategories(@RequestBody DeleteRequest deleteRequest) {
        Long categoriesId = deleteRequest.getId();
        ThrowUtils.throwIf(categoriesId == null || categoriesId <= 0, ResultCode.PARAMS_ERROR);
        boolean result = categoriesService.deleteCategories(categoriesId);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新笔记分类")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCategories(@RequestBody CategoriesUpdateRequest categoriesUpdateRequest) {
        ThrowUtils.throwIf(categoriesUpdateRequest == null, ResultCode.PARAMS_ERROR);
        Categories categories = new Categories();
        BeanUtils.copyProperties(categoriesUpdateRequest, categories);
        categoriesService.validCategories(categories, false);
        boolean result = categoriesService.updateById(categories);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "分页获取笔记分类列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<CategoriesVO>> listCategoriesVOByPage(@RequestBody CategoriesQueryRequest categoriesQueryRequest) {
        ThrowUtils.throwIf(categoriesQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = categoriesQueryRequest.getCurrent();
        long size = categoriesQueryRequest.getPageSize();
        // 查询数据库
        Page<Categories> categoriesPage = categoriesService.page(new Page<>(current, size),
                categoriesService.getQueryWrapper(categoriesQueryRequest));
        // 获取封装
        return ResultUtils.success(categoriesService.getCategoriesVOPage(categoriesPage));
    }
}
