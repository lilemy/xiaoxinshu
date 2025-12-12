package com.lilemy.xiaoxinshu.model.vo.articletagrel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章标签关系脱敏信息
 *
 * @author lilemy
 * @date 2025-12-12 10:34
 */
@Data
@Schema(name = "ArtArticleTagRelVo", description = "文章标签关系脱敏信息")
public class ArtArticleTagRelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4039730116054069508L;

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 文章 id
     */
    @Schema(description = "文章 id")
    private Long articleId;

    /**
     * 标签 id
     */
    @Schema(description = "标签 id")
    private Long tagId;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    private String tagName;
}
