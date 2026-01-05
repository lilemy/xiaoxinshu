package com.lilemy.xiaoxinshu.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleTagRel;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagNameVo;
import com.lilemy.xiaoxinshu.model.vo.articletagrel.ArtArticleTagRelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lilemy
 * @description 针对表【art_article_tag_rel(文章对应标签)】的数据库操作Mapper
 * @createDate 2025-12-09 18:30:38
 * @Entity com.lilemy.xiaoxinshu.model.entity.ArtArticleTagRel
 */
public interface ArtArticleTagRelMapper extends BaseMapper<ArtArticleTagRel> {

    /**
     * 根据文章 id 获取文章标签信息
     *
     * @param articleId 文章 id
     * @return 文章标签信息
     */
    List<ArtArticleTagNameVo> listArticleTagVoByArticleId(Long articleId);

    /**
     * 根据标签 id 获取文章标签关系信息
     *
     * @param tagId 标签 id
     * @return 文章标签关系信息
     */
    default List<ArtArticleTagRel> listArticleTagRelByTagId(Long tagId) {
        LambdaQueryWrapper<ArtArticleTagRel> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArtArticleTagRel::getTagId, tagId);
        return this.selectList(lqw);
    }

    /**
     * 根据文章 id 获取文章标签关系信息
     *
     * @param ids 文章 id 列表
     * @return 文章标签关系信息
     */
    List<ArtArticleTagRelVo> listArticleTagRelByArticleIds(@Param("ids") List<Long> ids);

    /**
     * 根据标签 id 列表获取文章 id 列表
     *
     * @param ids 标签 id 列表
     * @return 文章 id 列表
     */
    default List<Long> listArticleIdsByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }
        LambdaQueryWrapper<ArtArticleTagRel> lqw = new LambdaQueryWrapper<>();
        lqw.select(ArtArticleTagRel::getArticleId).in(ArtArticleTagRel::getTagId, ids);
        return this.selectList(lqw).stream().map(ArtArticleTagRel::getArticleId).toList();
    }

    /**
     * 根据文章 id 删除文章标签关系
     *
     * @param articleId 文章 id
     * @return 是否删除成功
     */
    default Boolean deleteByArticleId(Long articleId) {
        LambdaQueryWrapper<ArtArticleTagRel> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArtArticleTagRel::getArticleId, articleId);
        return SqlHelper.retBool(this.delete(lqw));
    }
}




