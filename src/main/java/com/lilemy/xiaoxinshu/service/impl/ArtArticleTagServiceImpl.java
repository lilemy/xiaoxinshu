package com.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.mapper.ArtArticleTagMapper;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagCreateRequest;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.articletag.ArtArticleTagUpdateRequest;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleTag;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagVo;
import com.lilemy.xiaoxinshu.service.ArtArticleTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章标签服务实现
 *
 * @author lilemy
 * @description 针对表【art_article_tag(文章标签)】的数据库操作Service实现
 * @createDate 2025-11-30 17:02:17
 */
@Service
public class ArtArticleTagServiceImpl extends ServiceImpl<ArtArticleTagMapper, ArtArticleTag>
        implements ArtArticleTagService {

    @Override
    public Long createArticleTag(ArtArticleTagCreateRequest req) {
        // 1.将请求体转换为实体
        ArtArticleTag articleTag = new ArtArticleTag();
        BeanUtils.copyProperties(req, articleTag);
        // 2.参数校验
        Long count = this.lambdaQuery()
                .eq(ArtArticleTag::getName, articleTag.getName())
                .count();
        ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "文章标签已存在");
        // 3.插入数据
        boolean save = this.save(articleTag);
        ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "创建文章标签失败，数据库异常");
        // 4.返回文章标签id
        return articleTag.getId();
    }

    @Override
    public Boolean updateArticleTag(ArtArticleTagUpdateRequest req) {
        // 1.将请求体转换为实体
        ArtArticleTag articleTag = new ArtArticleTag();
        BeanUtils.copyProperties(req, articleTag);
        // 2.校验数据是否存在
        ArtArticleTag oldArticleTag = this.getById(articleTag.getId());
        // 3.参数校验
        String name = req.getName();
        if (StringUtils.isNotBlank(name) && !name.equals(oldArticleTag.getName())) {
            Long count = this.lambdaQuery()
                    .eq(ArtArticleTag::getName, name)
                    .ne(ArtArticleTag::getId, articleTag.getId())
                    .count();
            ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "文章标签已存在");
        }
        // 4.更新数据
        boolean update = this.updateById(articleTag);
        ThrowUtils.throwIf(!update, ResultCode.SYSTEM_ERROR, "更新文章标签失败，数据库异常");
        return true;
    }

    @Override
    public Boolean deleteArticleTag(Long id) {
        // 1.校验数据是否存在
        ArtArticleTag articleTag = this.getById(id);
        ThrowUtils.throwIf(articleTag == null, ResultCode.NOT_FOUND_ERROR);
        // 2.删除数据
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ResultCode.SYSTEM_ERROR, "删除文章标签失败，数据库异常");
        return true;
    }

    @Override
    public ArtArticleTagVo getArticleTagVo(ArtArticleTag articleTag) {
        if (articleTag == null) {
            return null;
        }
        ArtArticleTagVo articleTagVo = new ArtArticleTagVo();
        BeanUtils.copyProperties(articleTag, articleTagVo);
        return articleTagVo;
    }

    @Override
    public ArtArticleTagVo getArticleTagVo(Long articleTagId) {
        ArtArticleTag articleTag = null;
        if (articleTagId != null && articleTagId > 0) {
            articleTag = this.getById(articleTagId);
        }
        return this.getArticleTagVo(articleTag);
    }

    @Override
    public Page<ArtArticleTagVo> getArticleTagVoPage(ArtArticleTagQueryRequest req, PageQuery pageQuery) {
        Page<ArtArticleTag> articleTagPage = this.page(pageQuery.build(), this.getQueryWrapper(req));
        List<ArtArticleTag> articleTagList = articleTagPage.getRecords();
        Page<ArtArticleTagVo> articleTagVoPage = new Page<>(
                articleTagPage.getCurrent(),
                articleTagPage.getSize(),
                articleTagPage.getTotal());
        if (CollUtil.isEmpty(articleTagList)) {
            return articleTagVoPage;
        }
        // 填充信息
        List<ArtArticleTagVo> articleTagVoList = articleTagList.stream().map(articleTag -> {
            ArtArticleTagVo articleTagVo = new ArtArticleTagVo();
            BeanUtils.copyProperties(articleTag, articleTagVo);
            return articleTagVo;
        }).toList();
        articleTagVoPage.setRecords(articleTagVoList);
        return articleTagVoPage;
    }

    @Override
    public LambdaQueryWrapper<ArtArticleTag> getQueryWrapper(ArtArticleTagQueryRequest req) {
        LambdaQueryWrapper<ArtArticleTag> lqw = new LambdaQueryWrapper<>();
        if (req == null) {
            return lqw;
        }
        String name = req.getName();
        lqw.like(StringUtils.isNotBlank(name), ArtArticleTag::getName, name);
        return lqw;
    }

}




