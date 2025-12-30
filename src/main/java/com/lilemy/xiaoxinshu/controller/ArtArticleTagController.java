package com.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.annotation.RepeatSubmit;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.constant.UserConstant;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagCreateRequest;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagUpdateRequest;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleTag;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagVo;
import com.lilemy.xiaoxinshu.service.ArtArticleTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章标签接口
 *
 * @author lilemy
 * @date 2025-11-30 19:30
 */
@Slf4j
@RestController
@RequestMapping("/art/article/tag")
@Tag(name = "ArtArticleTagController", description = "文章标签接口")
public class ArtArticleTagController {

    @Resource
    private ArtArticleTagService articleTagService;

    @Operation(summary = "创建文章标签信息（仅管理员）")
    @RepeatSubmit()
    @PostMapping("/create")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Long> createArticleTag(@Validated @RequestBody ArtArticleTagCreateRequest req) {
        return ResultUtils.success(articleTagService.createArticleTag(req));
    }

    @Operation(summary = "更新文章标签信息（仅管理员）")
    @RepeatSubmit()
    @PutMapping("/update")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> updateArticleTag(@Validated @RequestBody ArtArticleTagUpdateRequest req) {
        return ResultUtils.success(articleTagService.updateArticleTag(req));
    }

    @Operation(summary = "删除文章标签信息（仅管理员）")
    @DeleteMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN)
    public BaseResponse<Boolean> deleteArticleTag(@NotNull(message = "主键不能为空")
                                                  @PathVariable Long id) {
        return ResultUtils.success(articleTagService.deleteArticleTag(id));
    }

    @Operation(summary = "获取文章标签信息")
    @GetMapping("/{id}")
    public BaseResponse<ArtArticleTagVo> getArticleTag(@NotNull(message = "主键不能为空")
                                                       @PathVariable Long id) {
        ArtArticleTag articleTag = articleTagService.getById(id);
        return ResultUtils.success(articleTagService.getArticleTagVo(articleTag));
    }

    @Operation(summary = "分页获取文章标签信息")
    @GetMapping("/page")
    public BaseResponse<Page<ArtArticleTagVo>> listArticleTagPage(ArtArticleTagQueryRequest req,
                                                                  PageQuery pageQuery) {
        return ResultUtils.success(articleTagService.getArticleTagVoPage(req, pageQuery));
    }

    @Operation(summary = "获取文章标签信息列表")
    @GetMapping("/list")
    public BaseResponse<List<ArtArticleTagVo>> listArticleTag(ArtArticleTagQueryRequest req) {
        return ResultUtils.success(articleTagService.getArticleTagList(req));
    }
}
