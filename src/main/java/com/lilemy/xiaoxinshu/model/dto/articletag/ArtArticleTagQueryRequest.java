package com.lilemy.xiaoxinshu.model.dto.articletag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章标签查询请求
 *
 * @author lilemy
 * @date 2025-11-30 19:01
 */
@Data
@Schema(name = "ArtArticleTagQueryRequest", description = "文章标签查询请求")
public class ArtArticleTagQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4795589835684038215L;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    private String name;
}
