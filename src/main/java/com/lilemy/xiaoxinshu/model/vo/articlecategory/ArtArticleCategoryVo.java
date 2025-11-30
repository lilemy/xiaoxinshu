package com.lilemy.xiaoxinshu.model.vo.articlecategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章分类脱敏信息
 *
 * @author lilemy
 * @date 2025-11-03 23:02
 */
@Data
@Schema(name = "ArtArticleCategoryVo", description = "文章分类脱敏信息")
public class ArtArticleCategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8047917050187220868L;

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

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
