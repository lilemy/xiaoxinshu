package cn.lilemy.xiaoxinshu.model.dto.categories;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 笔记分类更新请求体
 */
@Data
public class CategoriesUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1218568283590613222L;

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
}
