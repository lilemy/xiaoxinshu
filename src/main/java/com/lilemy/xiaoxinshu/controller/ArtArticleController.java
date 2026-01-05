package com.lilemy.xiaoxinshu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.annotation.RepeatSubmit;
import com.lilemy.xiaoxinshu.common.BaseResponse;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultUtils;
import com.lilemy.xiaoxinshu.model.dto.article.*;
import com.lilemy.xiaoxinshu.model.vo.article.*;
import com.lilemy.xiaoxinshu.service.ArtArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章接口
 *
 * @author lilemy
 * @date 2025-12-12 16:14
 */
@Slf4j
@RestController
@RequestMapping("/art/article")
@Tag(name = "ArtArticleController", description = "文章接口")
public class ArtArticleController {

    @Resource
    private ArtArticleService articleService;

    @Operation(summary = "创建文章信息")
    @RepeatSubmit()
    @PostMapping("/create")
    public BaseResponse<Long> createArticle(@Validated @RequestBody ArtArticleCreateRequest req) {
        return ResultUtils.success(articleService.createArticle(req));
    }

    @Operation(summary = "更新文章信息")
    @RepeatSubmit()
    @PutMapping("/update")
    public BaseResponse<Boolean> updateArticle(@Validated @RequestBody ArtArticleUpdateRequest req) {
        return ResultUtils.success(articleService.updateArticle(req));
    }

    @Operation(summary = "删除文章信息")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteArticle(@NotNull(message = "主键不能为空")
                                               @PathVariable Long id) {
        return ResultUtils.success(articleService.deleteArticle(id));
    }

    @Operation(summary = "获取文章信息")
    @GetMapping("/{id}")
    public BaseResponse<ArtArticleVo> getArticle(@NotNull(message = "主键不能为空")
                                                 @PathVariable Long id) {
        return ResultUtils.success(articleService.getArticleVo(id));
    }

    @Operation(summary = "获取文章详情信息")
    @GetMapping("/detail/{id}")
    public BaseResponse<ArtArticleDetailVo> getArticleDetail(@NotNull(message = "主键不能为空")
                                                             @PathVariable Long id) {
        return ResultUtils.success(articleService.getArticleDetailVo(id));
    }

    @Operation(summary = "分页获取文章信息")
    @GetMapping("/page")
    public BaseResponse<Page<ArtArticleVo>> listArticlePage(ArtArticleQueryRequest req,
                                                            PageQuery pageQuery) {
        return ResultUtils.success(articleService.getArticleVoPage(req, pageQuery));
    }

    @Operation(summary = "分页获取文章归档信息")
    @GetMapping("/archive/page")
    public BaseResponse<Page<ArtArticleArchiveVo>> listArticleArchivePage(ArtArticleQueryRequest req,
                                                                          PageQuery pageQuery) {
        return ResultUtils.success(articleService.getArticleArchiveVoPage(req, pageQuery));
    }

    @Operation(summary = "根据文章分类分页获取文章信息")
    @GetMapping("/category/page")
    public BaseResponse<Page<ArtArticleByCategoryVo>> listArticleByCategoryPage(ArtArticleByCategoryQueryRequest req,
                                                                                PageQuery pageQuery) {
        return ResultUtils.success(articleService.getArticleVoByCategoryPage(req, pageQuery));
    }

    @Operation(summary = "根据文章标签分页获取文章信息")
    @GetMapping("/tag/page")
    public BaseResponse<Page<ArtArticleByTagVo>> listArticleByTagPage(ArtArticleByTagQueryRequest req,
                                                                      PageQuery pageQuery) {
        return ResultUtils.success(articleService.getArticleVoByTagPage(req, pageQuery));
    }
}
