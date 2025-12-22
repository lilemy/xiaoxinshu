package com.lilemy.xiaoxinshu.model.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * 文章归档详情
 *
 * @author lilemy
 * @date 2025-12-15 10:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ArtArticleArchiveDetailVo", description = "文章归档详情")
public class ArtArticleArchiveDetailVo {

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
     * 发布的月份
     */
    @Schema(description = "发布的月份")
    private YearMonth createMonth;
}
