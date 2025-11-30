package com.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryCreateRequest;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryUpdateRequest;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleCategory;
import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryVo;

/**
 * 文章分类服务
 *
 * @author lilemy
 * @description 针对表【art_article_category(文章分类)】的数据库操作Service
 * @createDate 2025-11-03 22:40:43
 */
public interface ArtArticleCategoryService extends IService<ArtArticleCategory> {

    /**
     * 创建文章分类
     *
     * @param req 文章分类创建请求体
     * @return 新文章分类id
     */
    Long createArticleCategory(ArtArticleCategoryCreateRequest req);

    /**
     * 更新文章分类
     *
     * @param req 文章分类更新请求体
     * @return 是否更新成功
     */
    Boolean updateArticleCategory(ArtArticleCategoryUpdateRequest req);

    /**
     * 删除文章分类
     *
     * @param id 文章分类id
     * @return 是否删除成功
     */
    Boolean deleteArticleCategory(Long id);

    /**
     * 获取文章分类脱敏信息
     *
     * @param articleCategory 文章分类信息
     * @return 脱敏后的文章分类信息
     */
    ArtArticleCategoryVo getArticleCategoryVo(ArtArticleCategory articleCategory);

    /**
     * 获取文章分类脱敏信息
     *
     * @param articleCategoryId 文章分类id
     * @return 脱敏后的文章分类信息
     */
    ArtArticleCategoryVo getArticleCategoryVo(Long articleCategoryId);

    /**
     * 获取文章分类脱敏信息
     *
     * @param req       文章分类查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的文章分类信息
     */
    Page<ArtArticleCategoryVo> getArticleCategoryVoPage(ArtArticleCategoryQueryRequest req, PageQuery pageQuery);

    /**
     * 获取查询条件
     *
     * @param req 文章分类查询请求体
     * @return 查询条件
     */
    LambdaQueryWrapper<ArtArticleCategory> getQueryWrapper(ArtArticleCategoryQueryRequest req);

}
