package com.lilemy.xiaoxinshu.model.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;

/**
 * 文章归档信息
 *
 * @author lilemy
 * @date 2025-12-15 10:44
 */
@Data
@Schema(name = "ArtArticleArchiveVo", description = "文章归档信息")
public class ArtArticleArchiveVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -6930799505587503811L;

    /**
     * 归档的月份
     */
    @Schema(description = "归档的月份")
    private YearMonth month;

    /**
     * 归档的文章列表
     */
    @Schema(description = "归档的文章列表")
    private List<ArtArticleArchiveDetailVo> detailList;
}
