package com.lilemy.xiaoxinshu.model.vo.article;

import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章脱敏信息（标签）
 *
 * @author lilemy
 * @date 2025-12-17 18:14
 */
@Data
@Schema(name = "ArtArticleByTagVo", description = "文章脱敏信息（标签）")
public class ArtArticleByTagVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2461214795326641987L;

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
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 分类列表
     */
    @Schema(description = "分类列表")
    private List<ArtArticleCategoryVo> categoryList;
}
