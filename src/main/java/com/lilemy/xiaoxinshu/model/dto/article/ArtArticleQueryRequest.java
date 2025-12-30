package com.lilemy.xiaoxinshu.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章查询请求
 *
 * @author lilemy
 * @date 2025-12-09 19:21
 */
@Data
@Schema(name = "ArtArticleQueryRequest", description = "文章查询请求")
public class ArtArticleQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8874562381974192672L;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String title;

    /**
     * 文章摘要
     */
    @Schema(description = "文章摘要")
    private String summary;

    /**
     * 分类 id 列表
     */
    @Schema(description = "分类 id 列表")
    private List<Long> categoryIdList;

    /**
     * 标签 id 列表
     */
    @Schema(description = "标签 id 列表")
    private List<Long> tagIdList;
}
