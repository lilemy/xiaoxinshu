package com.lilemy.xiaoxinshu.model.vo.article;

import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryVo;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章脱敏信息
 *
 * @author lilemy
 * @date 2025-12-09 19:40
 */
@Data
@Schema(name = "ArtArticleVo", description = "文章脱敏信息")
public class ArtArticleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -3352446720964410155L;

    /**
     * 文章 id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String title;

    /**
     * 文章封面
     */
    @Schema(description = "文章封面")
    private String cover;

    /**
     * 文章摘要
     */
    @Schema(description = "文章摘要")
    private String summary;

    /**
     * 文章内容
     */
    @Schema(description = "文章内容")
    private String content;

    /**
     * 用户 id
     */
    @Schema(description = "用户 id")
    private Long userId;

    /**
     * 编辑时间
     */
    @Schema(description = "编辑时间")
    private LocalDateTime editTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 阅读次数
     */
    @Schema(description = "阅读次数")
    private Integer readNum;

    /**
     * 分类列表
     */
    @Schema(description = "分类列表")
    private List<ArtArticleCategoryVo> categoryList;

    /**
     * 分类 id 列表
     */
    @Schema(description = "分类 id 列表")
    private List<Long> categoryIdList;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<ArtArticleTagVo> tagList;

    /**
     * 标签 id 列表
     */
    @Schema(description = "标签 id 列表")
    private List<Long> tagIdList;
}
