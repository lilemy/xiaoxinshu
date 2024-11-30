package cn.lilemy.xiaoxinshu.model.dto.note;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 笔记更新请求体
 */
@Data
public class NoteUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -2620552135692793781L;

    /**
     * id
     */
    private Long id;

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
