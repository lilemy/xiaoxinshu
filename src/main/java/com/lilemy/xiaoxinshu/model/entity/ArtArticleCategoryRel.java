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
 * 文章所属分类
 *
 * @TableName art_article_category_rel
 */
@TableName(value = "art_article_category_rel")
@Data
@Schema(name = "ArtArticleCategoryRel", description = "文章所属分类")
public class ArtArticleCategoryRel implements Serializable {
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
     * 分类 id
     */
    @Schema(description = "分类 id")
    private Long categoryId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}