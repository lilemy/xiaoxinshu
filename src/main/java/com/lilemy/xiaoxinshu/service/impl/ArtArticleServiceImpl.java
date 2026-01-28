package com.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lilemy.xiaoxinshu.common.PageQuery;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.manager.markdown.MarkdownHelper;
import com.lilemy.xiaoxinshu.manager.rustfs.OssHelper;
import com.lilemy.xiaoxinshu.mapper.ArtArticleCategoryRelMapper;
import com.lilemy.xiaoxinshu.mapper.ArtArticleContentMapper;
import com.lilemy.xiaoxinshu.mapper.ArtArticleMapper;
import com.lilemy.xiaoxinshu.mapper.ArtArticleTagRelMapper;
import com.lilemy.xiaoxinshu.model.dto.article.*;
import com.lilemy.xiaoxinshu.model.entity.*;
import com.lilemy.xiaoxinshu.model.vo.article.*;
import com.lilemy.xiaoxinshu.model.vo.articlecategory.ArtArticleCategoryNameVo;
import com.lilemy.xiaoxinshu.model.vo.articlecategoryrel.ArtArticleCategoryRelVo;
import com.lilemy.xiaoxinshu.model.vo.articletag.ArtArticleTagNameVo;
import com.lilemy.xiaoxinshu.model.vo.articletagrel.ArtArticleTagRelVo;
import com.lilemy.xiaoxinshu.model.vo.user.SysUserVo;
import com.lilemy.xiaoxinshu.service.ArtArticleCategoryService;
import com.lilemy.xiaoxinshu.service.ArtArticleService;
import com.lilemy.xiaoxinshu.service.ArtArticleTagService;
import com.lilemy.xiaoxinshu.service.SysUserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章服务实现
 *
 * @author lilemy
 * @description 针对表【art_article(文章)】的数据库操作Service实现
 * @createDate 2025-12-09 18:30:38
 */
@Service
public class ArtArticleServiceImpl extends ServiceImpl<ArtArticleMapper, ArtArticle>
        implements ArtArticleService {

    @Resource
    private ArtArticleContentMapper artArticleContentMapper;

    @Resource
    private ArtArticleCategoryService artArticleCategoryService;

    @Resource
    private ArtArticleCategoryRelMapper artArticleCategoryRelMapper;

    @Resource
    private ArtArticleTagService artArticleTagService;

    @Resource
    private ArtArticleTagRelMapper artArticleTagRelMapper;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private OssHelper ossHelper;

    /**
     * 创建文章
     *
     * @param req 文章创建请求体
     * @return 新文章 id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createArticle(ArtArticleCreateRequest req) {
        // 保存文章信息
        ArtArticle article = new ArtArticle();
        BeanUtils.copyProperties(req, article);
        // 设置默认值
        article.setEditTime(LocalDateTime.now());
        SysUser loginUser = sysUserService.getLoginUser();
        article.setUserId(loginUser.getId());
        boolean save = this.save(article);
        ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "创建文章失败，数据库异常");
        Long id = article.getId();
        // 保存文章内容
        String content = req.getContent();
        ArtArticleContent articleContent = new ArtArticleContent();
        articleContent.setArticleId(id);
        articleContent.setContent(content);
        int saveContent = artArticleContentMapper.insert(articleContent);
        ThrowUtils.throwIf(!SqlHelper.retBool(saveContent), ResultCode.SYSTEM_ERROR, "创建文章内容失败，数据库异常");
        // 校验文章分类是否存在
        List<Long> categoryIds = req.getCategoryIdList();
        List<ArtArticleCategory> categoryList = artArticleCategoryService.listByIds(categoryIds);
        ThrowUtils.throwIf(categoryList.size() != categoryIds.size(), ResultCode.PARAMS_ERROR, "文章分类不存在");
        // 添加文章分类关联
        List<ArtArticleCategoryRel> categoryRelList = categoryList.stream().map(category -> {
            ArtArticleCategoryRel rel = new ArtArticleCategoryRel();
            rel.setArticleId(id);
            rel.setCategoryId(category.getId());
            return rel;
        }).toList();
        List<BatchResult> categoryResult = artArticleCategoryRelMapper.insert(categoryRelList);
        ThrowUtils.throwIf(!SqlHelper.retBool(categoryResult), ResultCode.SYSTEM_ERROR, "创建文章分类关联失败，数据库异常");
        // 添加文章标签关联
        this.insertTags(id, req.getTagIdList());
        return id;
    }

    /**
     * 更新文章分类
     *
     * @param req 文章更新请求体
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArticle(ArtArticleUpdateRequest req) {
        Long id = req.getId();
        // 判断文章是否存在
        ArtArticle oldArticle = this.getById(id);
        ThrowUtils.throwIf(oldArticle == null, ResultCode.NOT_FOUND_ERROR);
        // 判断是否有更新权限
        ThrowUtils.throwIf(!this.checkPermission(oldArticle), ResultCode.NO_AUTH_ERROR);
        // 更新文章信息
        ArtArticle article = new ArtArticle();
        BeanUtils.copyProperties(req, article);
        // 更新编辑时间
        if (oldArticle.getUserId().equals(sysUserService.getLoginUser().getId())) {
            article.setEditTime(LocalDateTime.now());
        }
        boolean update = this.updateById(article);
        ThrowUtils.throwIf(!update, ResultCode.SYSTEM_ERROR, "更新文章失败，数据库异常");
        // 更新文章内容
        String content = req.getContent();
        Boolean contentResult = artArticleContentMapper.updateContentByArticleId(content, id);
        ThrowUtils.throwIf(!contentResult, ResultCode.SYSTEM_ERROR, "更新文章内容失败，数据库异常");
        // 校验文章分类是否存在
        List<Long> categoryIds = req.getCategoryIdList();
        List<ArtArticleCategory> categoryList = artArticleCategoryService.listByIds(categoryIds);
        ThrowUtils.throwIf(categoryList.size() != categoryIds.size(), ResultCode.PARAMS_ERROR, "文章分类不存在");
        // 先删除该文章关联的分类记录
        Boolean removeCategory = artArticleCategoryRelMapper.deleteByArticleId(id);
        ThrowUtils.throwIf(!removeCategory, ResultCode.SYSTEM_ERROR, "删除文章分类关联失败，数据库异常");
        // 添加文章分类关联
        List<ArtArticleCategoryRel> categoryRelList = categoryIds.stream().map(categoryId -> {
            ArtArticleCategoryRel rel = new ArtArticleCategoryRel();
            rel.setArticleId(id);
            rel.setCategoryId(categoryId);
            return rel;
        }).toList();
        List<BatchResult> categoryResult = artArticleCategoryRelMapper.insert(categoryRelList);
        ThrowUtils.throwIf(!SqlHelper.retBool(categoryResult), ResultCode.SYSTEM_ERROR, "创建文章分类关联失败，数据库异常");
        // 先删除该文章对应的标签
        Boolean removeTag = artArticleTagRelMapper.deleteByArticleId(id);
        ThrowUtils.throwIf(!removeTag, ResultCode.SYSTEM_ERROR, "删除文章标签关联失败，数据库异常");
        this.insertTags(id, req.getTagIdList());
        return true;
    }

    /**
     * 删除文章
     *
     * @param id 文章 id
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteArticle(Long id) {
        // 判断文章是否存在
        ArtArticle article = this.getById(id);
        ThrowUtils.throwIf(article == null, ResultCode.NOT_FOUND_ERROR);
        // 判断是否有删除权限
        ThrowUtils.throwIf(!this.checkPermission(article), ResultCode.NO_AUTH_ERROR);
        // 删除文章
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ResultCode.SYSTEM_ERROR, "删除文章失败，数据库异常");
        // 删除文章封面
        String cover = article.getCover();
        if (StringUtils.isNotBlank(cover)) {
            ossHelper.deleteFileByUrl(cover);
        }
        // 删除文章内容
        String content = artArticleContentMapper.selectContentByArticleId(id);
        if (StringUtils.isNotBlank(content)) {
            List<String> imageUrls = MarkdownHelper.extractImageUrls(content);
            imageUrls.forEach(url -> ossHelper.deleteFileByUrl(url));
        }
        Boolean contentResult = artArticleContentMapper.deleteByArticleId(id);
        ThrowUtils.throwIf(!contentResult, ResultCode.SYSTEM_ERROR, "删除文章内容失败，数据库异常");
        // 删除文章分类关联
        Boolean removeCategory = artArticleCategoryRelMapper.deleteByArticleId(id);
        ThrowUtils.throwIf(!removeCategory, ResultCode.SYSTEM_ERROR, "删除文章分类关联失败，数据库异常");
        // 删除文章标签关联
        Boolean removeTag = artArticleTagRelMapper.deleteByArticleId(id);
        ThrowUtils.throwIf(!removeTag, ResultCode.SYSTEM_ERROR, "删除文章标签关联失败，数据库异常");
        return true;
    }

    /**
     * 获取文章脱敏信息
     *
     * @param article 文章信息
     * @return 脱敏后的文章信息
     */
    @Override
    public ArtArticleVo getArticleVo(ArtArticle article) {
        ThrowUtils.throwIf(article == null, ResultCode.NOT_FOUND_ERROR);
        ArtArticleVo articleVo = new ArtArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        // 获取当前文章的内容
        String content = artArticleContentMapper.selectContentByArticleId(article.getId());
        articleVo.setContent(MarkdownHelper.convertMarkdown2Html(content));
        // 获取当前文章的分类列表
        List<ArtArticleCategoryNameVo> articleVoList = artArticleCategoryRelMapper.listArticleCategoryVoByArticleId(article.getId());
        articleVo.setCategoryList(articleVoList);
        // 获取当前文章的标签列表
        List<ArtArticleTagNameVo> tagVoList = artArticleTagRelMapper.listArticleTagVoByArticleId(article.getId());
        articleVo.setTagList(tagVoList);
        return articleVo;
    }

    /**
     * 获取文章脱敏信息
     *
     * @param articleId 文章 id
     * @return 脱敏后的文章信息
     */
    @Override
    public ArtArticleVo getArticleVo(Long articleId) {
        ArtArticle artArticle = null;
        if (articleId != null && articleId > 0) {
            artArticle = this.getById(articleId);
        }
        return this.getArticleVo(artArticle);
    }

    /**
     * 获取文章详情脱敏信息
     *
     * @param articleId 文章 id
     * @return 脱敏后的文章详情信息
     */
    @Override
    public ArtArticleDetailVo getArticleDetailVo(Long articleId) {
        ArtArticleVo articleVo = this.getArticleVo(articleId);
        ArtArticleDetailVo detailVo = new ArtArticleDetailVo();
        BeanUtils.copyProperties(articleVo, detailVo);
        // 获取当前文章发布用户
        Long userId = articleVo.getUserId();
        if (userId != null && userId > 0) {
            SysUserVo userVo = sysUserService.getUserVo(userId);
            detailVo.setUser(userVo);
        }
        // 获取上一个文章
        ArtArticle preArticle = this.lambdaQuery()
                .orderByAsc(ArtArticle::getId)
                .gt(ArtArticle::getId, articleId)
                .last("limit 1")
                .one();
        detailVo.setPreArticle(ArtArticleNameVo.buildVo(preArticle));
        // 获取下一个文章
        ArtArticle nextArticle = this.lambdaQuery()
                .orderByDesc(ArtArticle::getId)
                .lt(ArtArticle::getId, articleId)
                .last("limit 1")
                .one();
        detailVo.setNextArticle(ArtArticleNameVo.buildVo(nextArticle));
        return detailVo;
    }

    /**
     * 获取文章脱敏信息
     *
     * @param req       文章查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的文章信息
     */
    @Override
    public Page<ArtArticleVo> getArticleVoPage(ArtArticleQueryRequest req, PageQuery pageQuery) {
        Page<ArtArticle> articlePage = this.page(pageQuery.build(), this.getQueryWrapper(req));
        List<ArtArticle> articleList = articlePage.getRecords();
        Page<ArtArticleVo> articleVoPage = new Page<>(
                articlePage.getCurrent(),
                articlePage.getSize(),
                articlePage.getTotal());
        if (CollUtil.isEmpty(articleList)) {
            return articleVoPage;
        }
        // 获取文章 id 列表
        List<Long> articleIdList = articleList.stream().map(ArtArticle::getId).toList();
        // 获取文章分类关系列表
        List<ArtArticleCategoryRelVo> categoryRelVoList = artArticleCategoryRelMapper.listArticleCategoryRelVoByArticleIds(articleIdList);
        Map<Long, List<ArtArticleCategoryRelVo>> categoryRelMap = categoryRelVoList.stream()
                .collect(Collectors.groupingBy(ArtArticleCategoryRelVo::getArticleId));
        // 获取文章标签关系列表
        List<ArtArticleTagRelVo> tagVoList = artArticleTagRelMapper.listArticleTagRelByArticleIds(articleIdList);
        Map<Long, List<ArtArticleTagRelVo>> tagRelMap = tagVoList.stream()
                .collect(Collectors.groupingBy(ArtArticleTagRelVo::getArticleId));
        // 填充信息
        List<ArtArticleVo> articleVoList = articleList.stream().map(article -> {
            ArtArticleVo articleVo = new ArtArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            Long id = article.getId();
            // 获取文章分类
            if (categoryRelMap.containsKey(id)) {
                List<ArtArticleCategoryRelVo> relVoList = categoryRelMap.get(id);
                articleVo.setCategoryList(relVoList.stream().map(categoryRel -> {
                    ArtArticleCategoryNameVo categoryVo = new ArtArticleCategoryNameVo();
                    categoryVo.setId(categoryRel.getCategoryId());
                    categoryVo.setName(categoryRel.getCategoryName());
                    BeanUtils.copyProperties(categoryRel, categoryVo);
                    return categoryVo;
                }).toList());
                articleVo.setCategoryIdList(relVoList.stream().map(ArtArticleCategoryRelVo::getCategoryId).toList());
            }
            // 获取文章标签
            if (tagRelMap.containsKey(id)) {
                List<ArtArticleTagRelVo> relVoList = tagRelMap.get(id);
                articleVo.setTagList(relVoList.stream().map(tagRel -> {
                    ArtArticleTagNameVo tagVo = new ArtArticleTagNameVo();
                    tagVo.setId(tagRel.getTagId());
                    tagVo.setName(tagRel.getTagName());
                    return tagVo;
                }).toList());
                articleVo.setTagIdList(relVoList.stream().map(ArtArticleTagRelVo::getTagId).toList());
            }
            return articleVo;
        }).toList();
        articleVoPage.setRecords(articleVoList);
        return articleVoPage;
    }

    /**
     * 获取文章归档信息
     *
     * @param req       文章查询请求体
     * @param pageQuery 分页查询参数
     * @return 脱敏后的文章信息
     */
    @Override
    public Page<ArtArticleArchiveVo> getArticleArchiveVoPage(ArtArticleQueryRequest req, PageQuery pageQuery) {
        LambdaQueryWrapper<ArtArticle> lqw = this.getQueryWrapper(req);
        // 仅获取当前用户的文章
        SysUser loginUser = sysUserService.getLoginUser();
        lqw.eq(ArtArticle::getUserId, loginUser.getId());
        Page<ArtArticle> articlePage = this.page(pageQuery.build(), lqw);
        List<ArtArticle> articleList = articlePage.getRecords();
        Page<ArtArticleArchiveVo> articleArchiveVoPage = new Page<>(
                articlePage.getCurrent(),
                articlePage.getSize(),
                articlePage.getTotal());
        if (CollUtil.isEmpty(articleList)) {
            return articleArchiveVoPage;
        }
        List<ArtArticleArchiveVo> voList = new ArrayList<>();
        // 将创建时间转为年月
        List<ArtArticleArchiveDetailVo> detailVoList = articleList.stream().map(article -> {
            ArtArticleArchiveDetailVo detailVo = new ArtArticleArchiveDetailVo();
            BeanUtils.copyProperties(article, detailVo);
            YearMonth month = YearMonth.from(article.getCreateTime());
            detailVo.setCreateMonth(month);
            return detailVo;
        }).toList();
        // 按创建的月份进行分组
        Map<YearMonth, List<ArtArticleArchiveDetailVo>> detailVoMap = detailVoList.stream()
                .collect(Collectors.groupingBy(ArtArticleArchiveDetailVo::getCreateMonth));
        // 使用 TreeMap 按月份倒序排列
        Map<YearMonth, List<ArtArticleArchiveDetailVo>> sortedMap = new TreeMap<>(Collections.reverseOrder());
        sortedMap.putAll(detailVoMap);
        // 遍历排序后的 Map，将其转换为归档 VO
        sortedMap.forEach((k, v) -> {
            ArtArticleArchiveVo archiveVo = new ArtArticleArchiveVo();
            archiveVo.setMonth(k);
            archiveVo.setDetailList(v);
            voList.add(archiveVo);
        });
        articleArchiveVoPage.setRecords(voList);
        return articleArchiveVoPage;
    }

    /**
     * 根据分类获取文章脱敏信息
     *
     * @param req       根据分类查询文章请求体
     * @param pageQuery 分页查询参数
     * @return 文章脱敏信息
     */
    @Override
    public Page<ArtArticleByCategoryVo> getArticleVoByCategoryPage(ArtArticleByCategoryQueryRequest req, PageQuery pageQuery) {
        Long categoryId = req.getCategoryId();
        // 获取当前登录用户
        SysUser loginUser = sysUserService.getLoginUser();
        // 根据分类 id 获取文章
        return baseMapper.getArticleByCategory(pageQuery.build(), categoryId, loginUser.getId());
    }

    /**
     * 根据标签获取文章脱敏信息
     *
     * @param req       根据标签查询文章请求体
     * @param pageQuery 分页查询参数
     * @return 文章脱敏信息
     */
    @Override
    public Page<ArtArticleByTagVo> getArticleVoByTagPage(ArtArticleByTagQueryRequest req, PageQuery pageQuery) {
        Long tagId = req.getTagId();
        // 获取当前登录用户
        SysUser loginUser = sysUserService.getLoginUser();
        // 根据标签 id 获取文章
        return baseMapper.getArticleByTag(pageQuery.build(), tagId, loginUser.getId());
    }

    /**
     * 获取查询条件
     *
     * @param req 文章查询请求体
     * @return 查询条件
     */
    @Override
    public LambdaQueryWrapper<ArtArticle> getQueryWrapper(ArtArticleQueryRequest req) {
        LambdaQueryWrapper<ArtArticle> lqw = new LambdaQueryWrapper<>();
        if (req == null) {
            return lqw;
        }
        String title = req.getTitle();
        String summary = req.getSummary();
        List<Long> categoryIdList = req.getCategoryIdList();
        List<Long> tagIdList = req.getTagIdList();
        lqw.like(StringUtils.isNotBlank(title), ArtArticle::getTitle, title);
        lqw.like(StringUtils.isNotBlank(summary), ArtArticle::getSummary, summary);
        getQueryRel(lqw, categoryIdList, artArticleCategoryRelMapper.listArticleIdsByIds(categoryIdList));
        getQueryRel(lqw, tagIdList, artArticleTagRelMapper.listArticleIdsByIds(tagIdList));
        return lqw;
    }

    /**
     * 获取查询条件
     *
     * @param lqw        查询条件
     * @param tagIdList  标签 id 列表
     * @param articleIds 文章 id 列表
     */
    private void getQueryRel(LambdaQueryWrapper<ArtArticle> lqw, List<Long> tagIdList, List<Long> articleIds) {
        if (CollUtil.isNotEmpty(tagIdList)) {
            if (CollUtil.isNotEmpty(articleIds)) {
                lqw.in(ArtArticle::getId, articleIds);
            } else {
                lqw.eq(ArtArticle::getId, -1);
            }
        }
    }

    /**
     * 保存文章标签
     *
     * @param articleId 文章 id
     * @param tags      标签列表
     */
    private void insertTags(Long articleId, List<String> tags) {
        // 筛选提交的标签（表中不存在的标签）
        List<String> notExistTags;
        // 筛选提交的标签（表中已存在的标签）
        List<String> existedTags = null;
        // 查询出所有标签
        List<ArtArticleTag> allList = artArticleTagService.list();
        if (CollUtil.isEmpty(allList)) {
            notExistTags = tags;
        } else {
            // 表中已添加相关标签，则需要筛选
            List<String> tagIds = allList.stream().map(tagDO -> String.valueOf(tagDO.getId())).toList();
            // 通过标签 ID 来筛选，包含对应 ID 则表示提交的标签是表中存在的
            existedTags = tags.stream().filter(tagIds::contains).collect(Collectors.toList());
            // 否则则是不存在的
            notExistTags = tags.stream().filter(tag -> !tagIds.contains(tag)).collect(Collectors.toList());
            // 按字符串名称提交上来的标签，也有可能是表中已存在的，比如表中已经有了 Java 标签，用户提交了个 java 小写的标签，需要内部装换为 Java 标签
            Map<String, Long> tagNameIdMap = allList.stream().collect(Collectors.toMap(tag -> tag.getName().toLowerCase(), ArtArticleTag::getId));
            // 使用迭代器进行安全的删除操作
            Iterator<String> iterator = notExistTags.iterator();
            while (iterator.hasNext()) {
                String notExistTag = iterator.next();
                // 转小写, 若 Map 中相同的 key，则表示该新标签是重复标签
                if (tagNameIdMap.containsKey(notExistTag.toLowerCase())) {
                    // 从不存在的标签集合中清除
                    iterator.remove();
                    // 并将对应的 ID 添加到已存在的标签集合
                    existedTags.add(String.valueOf(tagNameIdMap.get(notExistTag.toLowerCase())));
                }
            }
        }
        // 将提交的上来的，已存在于表中的标签，文章-标签关联关系入库
        if (CollUtil.isNotEmpty(existedTags)) {
            List<ArtArticleTagRel> tagRelList = existedTags.stream().map(tagId -> {
                ArtArticleTagRel rel = new ArtArticleTagRel();
                rel.setArticleId(articleId);
                rel.setTagId(Long.valueOf(tagId));
                return rel;
            }).toList();
            // 批量插入
            List<BatchResult> insert = artArticleTagRelMapper.insert(tagRelList);
            ThrowUtils.throwIf(!SqlHelper.retBool(insert), ResultCode.SYSTEM_ERROR, "创建文章标签关联失败，数据库异常");
        }
        // 将提交的上来的，不存在于表中的标签，入库保存
        if (CollUtil.isNotEmpty(notExistTags)) {
            // 需要先将标签入库，拿到对应标签 ID 后，再把文章-标签关联关系入库
            List<ArtArticleTagRel> articleTagRelList = new ArrayList<>();
            notExistTags.forEach(tagName -> {
                ArtArticleTag tag = new ArtArticleTag();
                tag.setName(tagName);
                boolean save = artArticleTagService.save(tag);
                ThrowUtils.throwIf(!save, ResultCode.SYSTEM_ERROR, "创建文章标签失败，数据库异常");
                // 拿到保存的标签 ID
                Long tagId = tag.getId();
                // 文章-标签关联关系
                ArtArticleTagRel tagRel = new ArtArticleTagRel();
                tagRel.setArticleId(articleId);
                tagRel.setTagId(tagId);
                articleTagRelList.add(tagRel);
            });
            // 批量插入
            List<BatchResult> insert = artArticleTagRelMapper.insert(articleTagRelList);
            ThrowUtils.throwIf(!SqlHelper.retBool(insert), ResultCode.SYSTEM_ERROR, "创建文章标签关联失败，数据库异常");
        }
    }

    /**
     * 检查权限（判断用户是否为文章作者或者管理员）
     *
     * @param article 文章信息
     * @return 是否有权限
     */
    private boolean checkPermission(ArtArticle article) {
        // 1. 判空文章
        if (article == null) {
            return false;
        }
        // 2. 获取当前用户
        SysUser currentUser = sysUserService.getLoginUser();
        if (currentUser == null) {
            return false;
        }
        // 3. 获取文章作者
        Long userId = article.getUserId();
        if (userId == null) {
            return false;
        }
        // 4. 作者本人 或 管理员
        return userId.equals(currentUser.getId()) || sysUserService.isAdmin(currentUser);
    }
}




