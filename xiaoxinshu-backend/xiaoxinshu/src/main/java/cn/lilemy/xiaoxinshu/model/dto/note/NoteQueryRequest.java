package cn.lilemy.xiaoxinshu.model.dto.note;

import cn.lilemy.xiaoxinshucommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 查询笔记请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoteQueryRequest extends PageRequest implements Serializable {

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
     * 搜索词
     */
    private String searchText;

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
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 可见范围(0-公开, 1-仅对自己可见)
     */
    private Integer visible;

    /**
     * 是否要展示内容
     */
    private boolean needContent;
}
