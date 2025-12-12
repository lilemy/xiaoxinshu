package com.lilemy.xiaoxinshu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleContent;

/**
 * @author lilemy
 * @description 针对表【art_article_content(文章内容)】的数据库操作Mapper
 * @createDate 2025-12-09 18:30:38
 * @Entity com.lilemy.xiaoxinshu.model.entity.ArtArticleContent
 */
public interface ArtArticleContentMapper extends BaseMapper<ArtArticleContent> {

    /**
     * 根据文章 id 获取文章内容
     *
     * @param articleId 文章 id
     * @return 文章内容
     */
    default String selectContentByArticleId(Long articleId) {
        LambdaQueryWrapper<ArtArticleContent> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArtArticleContent::getArticleId, articleId);
        ArtArticleContent articleContent = this.selectOne(lqw);
        if (articleContent == null) {
            return null;
        }
        return articleContent.getContent();
    }

    /**
     * 根据文章 id 更新文章内容
     *
     * @param content   文章内容
     * @param articleId 文章 id
     * @return 是否更新成功
     */
    default Boolean updateContentByArticleId(String content, Long articleId) {
        LambdaUpdateWrapper<ArtArticleContent> luw = new LambdaUpdateWrapper<>();
        luw.eq(ArtArticleContent::getArticleId, articleId);
        luw.set(ArtArticleContent::getContent, content);
        return SqlHelper.retBool(this.update(luw));
    }

    /**
     * 根据文章 id 删除文章内容
     *
     * @param articleId 文章 id
     * @return 是否删除成功
     */
    default Boolean deleteByArticleId(Long articleId) {
        LambdaQueryWrapper<ArtArticleContent> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArtArticleContent::getArticleId, articleId);
        return SqlHelper.retBool(this.delete(lqw));
    }
}




