package com.lilemy.xiaoxinshu.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 根据分类查询文章请求体
 *
 * @author lilemy
 * @date 2025-12-17 15:03
 */
@Data
@Schema(name = "ArtArticleByCategoryQueryRequest", description = "根据分类查询文章请求体")
public class ArtArticleByCategoryQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2470807193526956250L;

    /**
     * 分类 id
     */
    @Schema(description = "分类 id")
    @NotNull(message = "分类 id 不能为空")
    private Long categoryId;
}
