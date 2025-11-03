package com.lilemy.xiaoxinshu.model.dto.articlecategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分类创建请求
 *
 * @author lilemy
 * @date 2025-11-03 22:59
 */
@Data
public class ArtArticleCategoryCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1270700869219097109L;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 30, message = "分类名称过长")
    private String name;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能小于0")
    private Integer sort;
}
