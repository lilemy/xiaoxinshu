package cn.lilemy.xiaoxinshu.model.dto.note;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 笔记审核请求体
 */
@Data
public class NoteReviewRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4473436963510516845L;

    /**
     * 题目 id
     */
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;
}
