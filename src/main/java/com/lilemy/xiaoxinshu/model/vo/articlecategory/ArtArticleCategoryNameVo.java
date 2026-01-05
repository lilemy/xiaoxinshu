package com.lilemy.xiaoxinshu.model.vo.articlecategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分类名称信息
 *
 * @author lilemy
 * @date 2026-01-05 17:18
 */
@Data
@Schema(name = "ArtArticleCategoryNameVo", description = "文章分类名称信息")
public class ArtArticleCategoryNameVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -9031511675917441352L;

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;
}
