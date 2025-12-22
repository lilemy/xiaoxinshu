package com.lilemy.xiaoxinshu.model.vo.article;

import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章脱敏信息（分类）
 *
 * @author lilemy
 * @date 2025-12-17 15:23
 */
@Data
@Schema(name = "ArtArticleByCategoryVo", description = "文章脱敏信息（分类）")
public class ArtArticleByCategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3700626828856034364L;

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
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<ArtArticleTagVo> tagList;
}
