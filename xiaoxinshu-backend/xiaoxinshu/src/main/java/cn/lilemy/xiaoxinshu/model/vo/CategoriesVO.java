package cn.lilemy.xiaoxinshu.model.vo;

import cn.lilemy.xiaoxinshucommon.model.entity.Categories;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 笔记分类封装
 */
@Data
public class CategoriesVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1247913094040859062L;

    /**
     * id
     */
    private Long id;

    /**
     * 笔记分类名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 封装类转对象
     *
     * @param categoriesVO 笔记分类脱敏信息
     * @return 笔记分类信息
     */
    public static Categories voToObj(CategoriesVO categoriesVO) {
        if (categoriesVO == null) {
            return null;
        }
        Categories categories = new Categories();
        BeanUtils.copyProperties(categoriesVO, categories);
        return categories;
    }

    /**
     * 对象转封装类
     *
     * @param categories 笔记分类信息
     * @return 笔记分类脱敏信息
     */
    public static CategoriesVO objToVo(Categories categories) {
        if (categories == null) {
            return null;
        }
        CategoriesVO categoriesVO = new CategoriesVO();
        BeanUtils.copyProperties(categories, categoriesVO);
        return categoriesVO;
    }
}
