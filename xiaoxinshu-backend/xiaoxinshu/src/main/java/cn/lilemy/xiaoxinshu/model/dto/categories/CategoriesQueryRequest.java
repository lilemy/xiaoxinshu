package cn.lilemy.xiaoxinshu.model.dto.categories;

import cn.lilemy.xiaoxinshucommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 查询笔记分类请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoriesQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4674386752195640482L;

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 笔记分类名称
     */
    private String name;

}
