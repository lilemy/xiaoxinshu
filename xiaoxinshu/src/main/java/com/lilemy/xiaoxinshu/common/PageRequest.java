package com.lilemy.xiaoxinshu.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询实体类
 *
 * @author lilemy
 * @date 2025/06/03 18:27
 */
@Data
@Schema(name = "PageRequest", description = "分页查询实体类")
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6095487855238622094L;

    /**
     * 当前页号
     */
    @Schema(description = "当前页号")
    private int current = 1;

    /**
     * 页面大小
     */
    @Schema(description = "页面大小")
    private int pageSize = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    @Schema(description = "排序顺序（默认降序）")
    private String sortOrder = "descend";

}
