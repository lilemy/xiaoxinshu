package com.lilemy.xiaoxinshu.model.vo.articlecategoryrel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分类关系脱敏信息
 *
 * @author lilemy
 * @date 2025-12-12 10:31
 */
@Data
@Schema(name = "ArtArticleCategoryRelVo", description = "文章分类关系脱敏信息")
public class ArtArticleCategoryRelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7182792651317082608L;

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 文章 id
     */
    @Schema(description = "文章 id")
    private Long articleId;

    /**
     * 分类 id
     */
    @Schema(description = "分类 id")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;
}
