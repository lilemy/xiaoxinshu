package com.lilemy.xiaoxinshu.model.vo.articletag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章标签脱敏信息
 *
 * @author lilemy
 * @date 2025-11-30 19:07
 */
@Data
@Schema(name = "ArtArticleTagVo", description = "文章标签脱敏信息")
public class ArtArticleTagVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -467365336766446422L;

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
