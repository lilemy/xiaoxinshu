package com.lilemy.xiaoxinshu.model.vo.articletag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章标签名称脱敏信息
 *
 * @author lilemy
 * @date 2026-01-05 17:16
 */
@Data
@Schema(name = "ArtArticleTagNameVo", description = "文章标签名称脱敏信息")
public class ArtArticleTagNameVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3103793180860843644L;

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    private String name;
}
