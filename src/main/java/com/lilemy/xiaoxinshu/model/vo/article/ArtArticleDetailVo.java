package com.lilemy.xiaoxinshu.model.vo.article;

import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryNameVo;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagNameVo;
import com.lilemy.xiaoxinshu.model.vo.user.SysUserVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章详情信息
 *
 * @author lilemy
 * @date 2026-01-05 17:33
 */
@Data
@Schema(name = "ArtArticleDetailVo", description = "文章详情信息")
public class ArtArticleDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -4689128349105934644L;

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
     * 发布用户
     */
    @Schema(description = "发布用户")
    private SysUserVo user;

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
    private List<ArtArticleCategoryNameVo> categoryList;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<ArtArticleTagNameVo> tagList;

    /**
     * 上一篇文章
     */
    @Schema(description = "上一篇文章")
    private ArtArticleNameVo preArticle;

    /**
     * 下一篇文章
     */
    @Schema(description = "下一篇文章")
    private ArtArticleNameVo nextArticle;
}
