package cn.lilemy.xiaoxinshu.model.dto.note;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 笔记创建请求体
 */
@Data
public class NoteCreateRequest implements Serializable {
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
     * 可见范围(0-公开, 1-仅对自己可见)
     */
    private Integer visible;

    /**
     * 是否置顶(0-未置顶 1-置顶)
     */
    private Integer isTop;

    /**
     * 笔记分类列表
     */
    private List<Long> categoriesIds;
}
