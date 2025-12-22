package com.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.model.dto.article.*;
import com.lilemy.xiaoxinshu.model.entity.ArtArticle;
import com.lilemy.xiaoxinshu.model.vo.article.ArtArticleArchiveVo;
import com.lilemy.xiaoxinshu.model.vo.article.ArtArticleByCategoryVo;
import com.lilemy.xiaoxinshu.model.vo.article.ArtArticleByTagVo;
import com.lilemy.xiaoxinshu.model.vo.article.ArtArticleVo;

/**
 * 文章服务
 *
 * @author lilemy
 * @description 针对表【art_article(文章)】的数据库操作Service
 * @createDate 2025-12-09 18:30:38
 */
public interface ArtArticleService extends IService<ArtArticle> {

    /**
     * 创建文章分类
     *
     * @param req 文章分类创建请求体
     * @return 新文章分类 id
     */
    Long createArticle(ArtArticleCreateRequest req);

    /**
     * 更新文章分类
     *
     * @param req 文章分类更新请求体
     * @return 是否更新成功
     */
    Boolean updateArticle(ArtArticleUpdateRequest req);

    /**
     * 删除文章分类
     *
     * @param id 文章分类 id
     * @return 是否删除成功
     */
    Boolean deleteArticle(Long id);

    /**
     * 获取文章分类脱敏信息
     *
     * @param article 文章分类信息
     * @return 脱敏后的文章分类信息
     */
    ArtArticleVo getArticleVo(ArtArticle article);

    /**
     * 获取文章分类脱敏信息
     *
     * @param articleId 文章分类 id
     * @return 脱敏后的文章分类信息
     */
    ArtArticleVo getArticleVo(Long articleId);

    /**
     * 获取文章分类脱敏信息
     *
     * @param req       文章分类查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的文章分类信息
     */
    Page<ArtArticleVo> getArticleVoPage(ArtArticleQueryRequest req, PageQuery pageQuery);

    /**
     * 获取文章归档信息
     *
     * @param req       文章分类查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的文章分类信息
     */
    Page<ArtArticleArchiveVo> getArticleArchiveVoPage(ArtArticleQueryRequest req, PageQuery pageQuery);

    /**
     * 根据分类获取文章脱敏信息
     *
     * @param req       根据分类查询文章请求体
     * @param pageQuery 分页查询参数
     * @return 文章脱敏信息
     */
    Page<ArtArticleByCategoryVo> getArticleVoByCategoryPage(ArtArticleByCategoryQueryRequest req, PageQuery pageQuery);

    /**
     * 根据标签获取文章脱敏信息
     *
     * @param req       根据标签查询文章请求体
     * @param pageQuery 分页查询参数
     * @return 文章脱敏信息
     */
    Page<ArtArticleByTagVo> getArticleVoByTagPage(ArtArticleByTagQueryRequest req, PageQuery pageQuery);

    /**
     * 获取查询条件
     *
     * @param req 文章分类查询请求体
     * @return 查询条件
     */
    LambdaQueryWrapper<ArtArticle> getQueryWrapper(ArtArticleQueryRequest req);
}
