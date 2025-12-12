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
 * 文章对应标签
 *
 * @TableName art_article_tag_rel
 */
@TableName(value = "art_article_tag_rel")
@Data
@Schema(name = "ArtArticleTagRel", description = "文章对应标签")
public class ArtArticleTagRel implements Serializable {
    /**
     * id
     */
    @Schema(description = "id")
    @TableId(type = IdType.ASSIGN_ID)
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

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}