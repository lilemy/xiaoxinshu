package com.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.mapper.ArtArticleCategoryMapper;
import com.lilemy.xiaoxinshu.mapper.ArtArticleCategoryRelMapper;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryCreateRequest;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryQueryRequest;
import com.lilemy.xiaoxinshu.model.dto.articlecategory.ArtArticleCategoryUpdateRequest;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleCategory;
import com.lilemy.xiaoxinshu.model.entity.ArtArticleCategoryRel;
import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryVo;
import com.lilemy.xiaoxinshu.service.ArtArticleCategoryService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章分类服务实现
 *
 * @author lilemy
 * @description 针对表【art_article_category(文章分类)】的数据库操作Service实现
 * @createDate 2025-11-03 22:40:43
 */
@Service
public class ArtArticleCategoryServiceImpl extends ServiceImpl<ArtArticleCategoryMapper, ArtArticleCategory>
        implements ArtArticleCategoryService {

    @Resource
    private ArtArticleCategoryRelMapper artArticleCategoryRelMapper;

    @Override
    public Long createArticleCategory(ArtArticleCategoryCreateRequest req) {
        // 1.将请求体转换为实体
        ArtArticleCategory articleCategory = new ArtArticleCategory();
        BeanUtils.copyProperties(req, articleCategory);
        // 2.参数校验
        Long count = this.lambdaQuery()
                .eq(ArtArticleCategory::getName, articleCategory.getName())
                .count();
        ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "文章分类已存在");
        // 3.插入数据
        boolean save = this.save(articleCategory);
        ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "创建文章分类失败，数据库异常");
        // 4.返回文章分类id
        return articleCategory.getId();
    }

    @Override
    public Boolean updateArticleCategory(ArtArticleCategoryUpdateRequest req) {
        // 1.将请求体转换为实体
        ArtArticleCategory articleCategory = new ArtArticleCategory();
        BeanUtils.copyProperties(req, articleCategory);
        // 2.校验数据是否存在
        ArtArticleCategory oldArticleCategory = this.getById(articleCategory.getId());
        // 3.参数校验
        String name = req.getName();
        if (StringUtils.isNotBlank(name) && !name.equals(oldArticleCategory.getName())) {
            Long count = this.lambdaQuery()
                    .eq(ArtArticleCategory::getName, name)
                    .ne(ArtArticleCategory::getId, articleCategory.getId())
                    .count();
            ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "文章分类已存在");
        }
        // 4.更新数据
        boolean update = this.updateById(articleCategory);
        ThrowUtils.throwIf(!update, ResultCode.SYSTEM_ERROR, "更新文章分类失败，数据库异常");
        return true;
    }

    @Override
    public Boolean deleteArticleCategory(Long id) {
        // 1.校验数据是否存在
        ArtArticleCategory articleCategory = this.getById(id);
        ThrowUtils.throwIf(articleCategory == null, ResultCode.NOT_FOUND_ERROR);
        // 2.校验数据是否被使用
        Long useCount = artArticleCategoryRelMapper.selectCount(new LambdaQueryWrapper<ArtArticleCategoryRel>()
                .eq(ArtArticleCategoryRel::getCategoryId, id));
        ThrowUtils.throwIf(useCount > 0, ResultCode.PARAMS_ERROR, "文章分类被使用，请先删除关联数据");
        // 3.删除数据
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ResultCode.SYSTEM_ERROR, "删除文章分类失败，数据库异常");
        return true;
    }

    @Override
    public ArtArticleCategoryVo getArticleCategoryVo(ArtArticleCategory articleCategory) {
        if (articleCategory == null) {
            return null;
        }
        ArtArticleCategoryVo articleCategoryVo = new ArtArticleCategoryVo();
        BeanUtils.copyProperties(articleCategory, articleCategoryVo);
        return articleCategoryVo;
    }

    @Override
    public ArtArticleCategoryVo getArticleCategoryVo(Long articleCategoryId) {
        ArtArticleCategory articleCategory = null;
        if (articleCategoryId != null && articleCategoryId > 0) {
            articleCategory = this.getById(articleCategoryId);
        }
        return this.getArticleCategoryVo(articleCategory);
    }

    @Override
    public Page<ArtArticleCategoryVo> getArticleCategoryVoPage(ArtArticleCategoryQueryRequest req, PageQuery pageQuery) {
        Page<ArtArticleCategory> articleCategoryPage = this.page(pageQuery.build(), this.getQueryWrapper(req));
        List<ArtArticleCategory> articleCategoryList = articleCategoryPage.getRecords();
        Page<ArtArticleCategoryVo> articleCategoryVoPage = new Page<>(
                articleCategoryPage.getCurrent(),
                articleCategoryPage.getSize(),
                articleCategoryPage.getTotal());
        if (CollUtil.isEmpty(articleCategoryList)) {
            return articleCategoryVoPage;
        }
        // 填充信息
        List<ArtArticleCategoryVo> articleCategoryVoList = articleCategoryList.stream().map(articleCategory -> {
            ArtArticleCategoryVo articleCategoryVo = new ArtArticleCategoryVo();
            BeanUtils.copyProperties(articleCategory, articleCategoryVo);
            return articleCategoryVo;
        }).toList();
        articleCategoryVoPage.setRecords(articleCategoryVoList);
        return articleCategoryVoPage;
    }

    @Override
    public List<ArtArticleCategoryVo> getArticleCategoryVoList(ArtArticleCategoryQueryRequest req) {
        List<ArtArticleCategory> articleCategoryList = this.list(this.getQueryWrapper(req));
        return articleCategoryList.stream().map(articleCategory ->
                BeanUtil.copyProperties(articleCategory, ArtArticleCategoryVo.class)).toList();
    }

    @Override
    public LambdaQueryWrapper<ArtArticleCategory> getQueryWrapper(ArtArticleCategoryQueryRequest req) {
        LambdaQueryWrapper<ArtArticleCategory> lqw = new LambdaQueryWrapper<>();
        if (req == null) {
            return lqw;
        }
        String name = req.getName();
        lqw.like(StringUtils.isNotBlank(name), ArtArticleCategory::getName, name);
        lqw.orderByAsc(ArtArticleCategory::getSort);
        return lqw;
    }
}




