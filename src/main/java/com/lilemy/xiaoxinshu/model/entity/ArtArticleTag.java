package com.lilemy.xiaoxinshu.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 文章标签
 *
 * @TableName art_article_tag
 */
@TableName(value = "art_article_tag")
@Data
@Schema(name = "art_article_tag", description = "文章标签")
public class ArtArticleTag implements Serializable {
    /**
     * id
     */
    @Schema(description = "id")
    @TableId(type = IdType.ASSIGN_ID)
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
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}