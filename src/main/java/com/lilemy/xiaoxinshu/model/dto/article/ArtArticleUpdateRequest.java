package com.lilemy.xiaoxinshu.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章更新请求
 *
 * @author lilemy
 * @date 2025-12-09 19:19
 */
@Data
@Schema(name = "ArtArticleUpdateRequest", description = "文章更新请求")
public class ArtArticleUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -9156050225833721023L;

    /**
     * 文章 id
     */
    @Schema(description = "id")
    @NotNull(message = "id 不能为空")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    @NotBlank(message = "文章标题不能为空")
    @Length(min = 1, max = 40, message = "文章标题字数需大于 1 小于 40")
    private String title;

    /**
     * 文章封面
     */
    @Schema(description = "文章封面")
    private String cover;

    /**
     * 文章摘要
     */
    @Schema(description = "文章摘要")
    private String summary;

    /**
     * 文章内容
     */
    @Schema(description = "文章内容")
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 文章分类 id
     */
    @Schema(description = "文章分类 id")
    @NotEmpty(message = "文章分类不能为空")
    @Size(max = 5, message = "文章分类数量不能超过 5 个")
    private List<Long> categoryIds;

    /**
     * 文章标签
     */
    @Schema(description = "文章标签")
    @NotEmpty(message = "文章标签不能为空")
    @Size(max = 10, message = "文章标签数量不能超过 10 个")
    private List<String> tags;
}
