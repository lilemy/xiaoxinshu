package com.lilemy.xiaoxinshu.model.dto.articlecategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分类更新请求
 *
 * @author lilemy
 * @date 2025-11-03 23:00
 */
@Data
@Schema(name = "ArtArticleCategoryUpdateRequest", description = "文章分类更新请求")
public class ArtArticleCategoryUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 823449365140238106L;

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    @Size(max = 30, message = "分类名称过长")
    private String name;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
}
