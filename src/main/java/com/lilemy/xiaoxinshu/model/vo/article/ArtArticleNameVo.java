package com.lilemy.xiaoxinshu.model.vo.article;

import com.lilemy.xiaoxinshu.model.entity.ArtArticle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 文章名称信息
 *
 * @author lilemy
 * @date 2026-01-05 17:46
 */
@Data
@Schema(name = "ArtArticleNameVo", description = "文章名称信息")
public class ArtArticleNameVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 6583602359799951706L;

    /**
     * 文章 id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String title;

    /**
     * 构建文章名称信息
     *
     * @param article 文章信息
     * @return 文章名称信息
     */
    public static ArtArticleNameVo buildVo(ArtArticle article) {
        ArtArticleNameVo vo = new ArtArticleNameVo();
        if (Objects.isNull(article)) {
            return vo;
        }
        vo.setId(article.getId());
        vo.setTitle(article.getTitle());
        return vo;
    }
}
