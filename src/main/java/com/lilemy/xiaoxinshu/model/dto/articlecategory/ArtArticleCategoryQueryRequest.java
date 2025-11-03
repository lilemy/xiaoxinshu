package com.lilemy.xiaoxinshu.model.dto.articlecategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分类查询请求
 *
 * @author lilemy
 * @date 2025-11-03 23:00
 */
@Data
public class ArtArticleCategoryQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5589844987580057143L;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;
}
