package cn.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lilemy.xiaoxinshu.model.dto.categories.CategoriesQueryRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.Categories;
import cn.lilemy.xiaoxinshu.model.vo.CategoriesVO;

/**
 * @author qq233
 * @description 针对表【categories(笔记分类)】的数据库操作Service
 */
public interface CategoriesService extends IService<Categories> {

    /**
     * 校验数据
     *
     * @param categories 笔记分类信息
     * @param add        对创建的数据进行校验
     */
    void validCategories(Categories categories, boolean add);

    /**
     * 笔记分类删除
     *
     * @param categoriesId 笔记分类 id
     * @return 是否成功删除
     */
    boolean deleteCategories(Long categoriesId);

    /**
     * 构建笔记分类查询条件
     *
     * @param categoriesQueryRequest 笔记分类查询请求体
     * @return 笔记分类查询条件
     */
    QueryWrapper<Categories> getQueryWrapper(CategoriesQueryRequest categoriesQueryRequest);

    /**
     * 分页获取笔记信息封装
     *
     * @param categoriesPage 笔记信息分页
     * @return 笔记封装信息分页
     */
    Page<CategoriesVO> getCategoriesVOPage(Page<Categories> categoriesPage);

}
