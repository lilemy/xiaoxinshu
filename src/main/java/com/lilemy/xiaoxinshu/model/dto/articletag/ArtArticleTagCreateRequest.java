package com.lilemy.xiaoxinshu.model.dto.articletag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章标签创建请求
 *
 * @author lilemy
 * @date 2025-11-30 19:00
 */
@Data
@Schema(name = "ArtArticleTagCreateRequest", description = "文章标签创建请求")
public class ArtArticleTagCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1348186968922739524L;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 20, message = "标签名称过长")
    private String name;
}
