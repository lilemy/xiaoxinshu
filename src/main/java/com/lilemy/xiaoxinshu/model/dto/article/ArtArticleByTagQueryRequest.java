package com.lilemy.xiaoxinshu.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 根据标签查询文章请求体
 *
 * @author lilemy
 * @date 2025-12-17 18:10
 */
@Data
@Schema(name = "ArtArticleByTagQueryRequest", description = "根据标签查询文章请求体")
public class ArtArticleByTagQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7317265578473880288L;

    /**
     * 标签 id
     */
    @Schema(description = "标签 id")
    @NotNull(message = "标签 id 不能为空")
    private Long tagId;
}
