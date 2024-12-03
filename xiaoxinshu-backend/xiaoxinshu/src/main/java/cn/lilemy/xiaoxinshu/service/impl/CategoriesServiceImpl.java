package cn.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.mapper.CategoriesMapper;
import cn.lilemy.xiaoxinshu.model.dto.categories.CategoriesQueryRequest;
import cn.lilemy.xiaoxinshu.model.entity.Categories;
import cn.lilemy.xiaoxinshu.model.vo.CategoriesVO;
import cn.lilemy.xiaoxinshu.service.CategoriesService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshu.util.SqlUtils;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qq233
 * @description 针对表【categories(笔记分类)】的数据库操作Service实现
 */
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
        implements CategoriesService {

    @Resource
    private UserService userService;

    @Override
    public void validCategories(Categories categories, boolean add) {
        ThrowUtils.throwIf(categories == null, ResultCode.PARAMS_ERROR);
        // 从对象中取值
        String name = categories.getName();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ResultCode.PARAMS_ERROR, "分类名称不能为空");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(name)) {
            ThrowUtils.throwIf(name.length() > 20, ResultCode.PARAMS_ERROR, "分类名称过长");
        }
    }

    @Override
    public boolean deleteCategories(Long categoriesId) {
        // todo 关联删除笔记分类关系
        Categories categories = this.getById(categoriesId);
        ThrowUtils.throwIf(categories == null, ResultCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.removeById(categoriesId);
        ThrowUtils.throwIf(!result, ResultCode.SUCCESS);
        return true;
    }

    @Override
    public QueryWrapper<Categories> getQueryWrapper(CategoriesQueryRequest categoriesQueryRequest) {
        QueryWrapper<Categories> queryWrapper = new QueryWrapper<>();
        if (categoriesQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = categoriesQueryRequest.getId();
        Long notId = categoriesQueryRequest.getNotId();
        String name = categoriesQueryRequest.getName();
        String sortField = categoriesQueryRequest.getSortField();
        String sortOrder = categoriesQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public Page<CategoriesVO> getCategoriesVOPage(Page<Categories> categoriesPage) {
        List<Categories> categoriesList = categoriesPage.getRecords();
        Page<CategoriesVO> categoriesVOPage = new Page<>(categoriesPage.getCurrent(), categoriesPage.getSize());
        if (CollUtil.isEmpty(categoriesList)) {
            return categoriesVOPage;
        }
        List<CategoriesVO> categoriesVOList = categoriesList.stream().map(CategoriesVO::objToVo).toList();
        categoriesVOPage.setRecords(categoriesVOList);
        return categoriesVOPage;
    }
}




