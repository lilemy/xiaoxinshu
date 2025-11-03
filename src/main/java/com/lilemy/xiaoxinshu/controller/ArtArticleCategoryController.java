package com.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.annotation.RepeatSubmit;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryCreateRequest;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryUpdateRequest;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleCategory;
import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryVo;
import com.lilemy.xiaoxinshu.service.ArtArticleCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章分类接口
 *
 * @author lilemy
 * @date 2025-11-03 23:12
 */
@Slf4j
@RestController
@RequestMapping("/art/article/category")
@Tag(name = "ArtArticleCategoryController", description = "文章分类接口")
public class ArtArticleCategoryController {

    @Resource
    private ArtArticleCategoryService articleCategoryService;

    @Operation(summary = "创建文章分类信息（仅管理员）")
    @RepeatSubmit()
    @PostMapping("/create")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Long> createArticleCategory(@Validated @RequestBody ArtArticleCategoryCreateRequest req) {
        return ResultUtils.success(articleCategoryService.createArticleCategory(req));
    }

    @Operation(summary = "更新文章分类信息（仅管理员）")
    @RepeatSubmit()
    @PutMapping("/update")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> updateArticleCategory(@Validated @RequestBody ArtArticleCategoryUpdateRequest req) {
        return ResultUtils.success(articleCategoryService.updateArticleCategory(req));
    }

    @Operation(summary = "删除文章分类信息（仅管理员）")
    @DeleteMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> deleteArticleCategory(@NotNull(message = "主键不能为空")
                                                       @PathVariable Long id) {
        return ResultUtils.success(articleCategoryService.deleteArticleCategory(id));
    }

    @Operation(summary = "获取文章分类信息")
    @GetMapping("/{id}")
    public BaseResponse<ArtArticleCategoryVo> getArticleCategory(@NotNull(message = "主键不能为空")
                                                                 @PathVariable Long id) {
        ArtArticleCategory articleCategory = articleCategoryService.getById(id);
        return ResultUtils.success(articleCategoryService.getArticleCategoryVo(articleCategory));
    }

    @Operation(summary = "分页获取文章分类信息")
    @GetMapping("/page")
    public BaseResponse<Page<ArtArticleCategoryVo>> listArticleCategoryPage(ArtArticleCategoryQueryRequest req,
                                                                            PageQuery pageQuery) {
        return ResultUtils.success(articleCategoryService.getArticleCategoryVoPage(req, pageQuery));
    }
}
