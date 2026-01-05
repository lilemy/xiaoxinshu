package com.lilemy.xiaoxinshu.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleCategoryRel;
import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryNameVo;
import com.lilemy.xiaoxinshu.model.vo.articlecategoryrel.ArtArticleCategoryRelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lilemy
 * @description 针对表【art_article_category_rel(文章所属分类)】的数据库操作Mapper
 * @createDate 2025-12-09 18:30:38
 * @Entity com.lilemy.xiaoxinshu.model.entity.ArtArticleCategoryRel
 */
public interface ArtArticleCategoryRelMapper extends BaseMapper<ArtArticleCategoryRel> {

    /**
     * 根据文章 id 获取文章分类信息
     *
     * @param articleId 文章 id
     * @return 文章分类信息
     */
    List<ArtArticleCategoryNameVo> listArticleCategoryVoByArticleId(Long articleId);

    /**
     * 根据分类 id 获取文章分类关系信息
     *
     * @param categoryId 分类 id
     * @return 文章分类关系信息
     */
    default List<ArtArticleCategoryRel> listArticleCategoryRelByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ArtArticleCategoryRel> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArtArticleCategoryRel::getCategoryId, categoryId);
        return this.selectList(lqw);
    }

    /**
     * 根据文章 id 获取文章分类关系信息
     *
     * @param ids 文章 id 列表
     * @return 文章分类关系信息
     */
    List<ArtArticleCategoryRelVo> listArticleCategoryRelVoByArticleIds(@Param("ids") List<Long> ids);

    /**
     * 根据分类 id 列表获取文章 id 列表
     *
     * @param ids 分类 id 列表
     * @return 文章 id 列表
     */
    default List<Long> listArticleIdsByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }
        LambdaQueryWrapper<ArtArticleCategoryRel> lqw = new LambdaQueryWrapper<>();
        lqw.select(ArtArticleCategoryRel::getArticleId).in(ArtArticleCategoryRel::getCategoryId, ids);
        return this.selectList(lqw).stream().map(ArtArticleCategoryRel::getArticleId).toList();
    }

    /**
     * 根据文章 id 删除文章分类关系信息
     *
     * @param articleId 文章 id
     * @return 是否成功
     */
    default Boolean deleteByArticleId(Long articleId) {
        LambdaQueryWrapper<ArtArticleCategoryRel> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArtArticleCategoryRel::getArticleId, articleId);
        return SqlHelper.retBool(this.delete(lqw));
    }
}




