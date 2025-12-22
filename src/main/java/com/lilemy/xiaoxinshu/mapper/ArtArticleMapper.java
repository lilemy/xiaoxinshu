package com.lilemy.xiaoxinshu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.model.entity.ArtArticle;
import com.lilemy.xiaoxinshu.model.vo.article.ArtArticleByCategoryVo;
import com.lilemy.xiaoxinshu.model.vo.article.ArtArticleByTagVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author lilemy
 * @description 针对表【art_article(文章)】的数据库操作Mapper
 * @createDate 2025-12-09 18:30:38
 * @Entity com.lilemy.xiaoxinshu.model.entity.ArtArticle
 */
public interface ArtArticleMapper extends BaseMapper<ArtArticle> {

    /**
     * 根据分类 id 获取文章
     *
     * @param page       分页参数
     * @param categoryId 分类 id
     * @param userId     用户 id
     * @return 文章列表
     */
    Page<ArtArticleByCategoryVo> getArticleByCategory(@Param("page") Page<ArtArticle> page,
                                                      @Param("categoryId") Long categoryId, @Param("userId") Long userId);

    /**
     * 根据标签 id 获取文章
     *
     * @param page   分页参数
     * @param tagId  标签 id
     * @param userId 用户 id
     * @return 文章列表
     */
    Page<ArtArticleByTagVo> getArticleByTag(@Param("page") Page<ArtArticle> page,
                                            @Param("tagId") Long tagId, @Param("userId") Long userId);
}




