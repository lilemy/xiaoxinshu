package com.lilemy.xiaoxinshu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章内容
 *
 * @TableName art_article_content
 */
@TableName(value = "art_article_content")
@Data
@Schema(name = "ArtArticleContent", description = "文章内容")
public class ArtArticleContent implements Serializable {
    /**
     * 文章内容id
     */
    @Schema(description = "id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章id
     */
    @Schema(description = "文章id")
    private Long articleId;

    /**
     * 文章正文
     */
    @Schema(description = "文章正文")
    private String content;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}