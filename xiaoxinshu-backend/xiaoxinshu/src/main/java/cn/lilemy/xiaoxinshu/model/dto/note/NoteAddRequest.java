
package cn.lilemy.xiaoxinshu.model.dto.note;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 笔记创建请求体
 */
@Data
public class NoteAddRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -4802518510633089106L;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 图片
     */
    private String picture;

    /**
     * 笔记分类列表
     */
    private List<Long> CategoriesList;
}
