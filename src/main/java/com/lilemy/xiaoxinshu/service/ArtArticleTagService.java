package com.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagCreateRequest;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagUpdateRequest;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleTag;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagVo;

/**
 * 文章标签服务
 *
 * @author lilemy
 * @description 针对表【art_article_tag(文章标签)】的数据库操作Service
 * @createDate 2025-11-30 17:02:17
 */
public interface ArtArticleTagService extends IService<ArtArticleTag> {

    /**
     * 创建文章标签
     *
     * @param req 文章标签创建请求体
     * @return 新文章标签id
     */
    Long createArticleTag(ArtArticleTagCreateRequest req);

    /**
     * 更新文章标签
     *
     * @param req 文章标签更新请求体
     * @return 是否更新成功
     */
    Boolean updateArticleTag(ArtArticleTagUpdateRequest req);

    /**
     * 删除文章标签
     *
     * @param id 文章标签id
     * @return 是否删除成功
     */
    Boolean deleteArticleTag(Long id);

    /**
     * 获取文章标签脱敏信息
     *
     * @param articleTag 文章标签信息
     * @return 脱敏后的文章标签信息
     */
    ArtArticleTagVo getArticleTagVo(ArtArticleTag articleTag);

    /**
     * 获取文章标签脱敏信息
     *
     * @param articleTagId 文章标签id
     * @return 脱敏后的文章标签信息
     */
    ArtArticleTagVo getArticleTagVo(Long articleTagId);

    /**
     * 获取文章标签脱敏信息
     *
     * @param req       文章标签查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的文章标签信息
     */
    Page<ArtArticleTagVo> getArticleTagVoPage(ArtArticleTagQueryRequest req, PageQuery pageQuery);

    /**
     * 获取查询条件
     *
     * @param req 文章标签查询请求体
     * @return 查询条件
     */
    LambdaQueryWrapper<ArtArticleTag> getQueryWrapper(ArtArticleTagQueryRequest req);

}
