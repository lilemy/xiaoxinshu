package cn.lilemy.xiaoxinshu.model.dto.note;

import cn.lilemy.xiaoxinshucommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 根据分类id获取笔记分页请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoteQueryByCategoriesRequest extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -4644954247961530560L;

    /**
     * 分类 id
     */
    private Long categoriesId;
}
