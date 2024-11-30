package cn.lilemy.xiaoxinshu.model.dto.categories;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 笔记分类创建请求体
 */
@Data
public class CategoriesCreateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1218568283590613222L;

    /**
     * 笔记分类名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;
}
