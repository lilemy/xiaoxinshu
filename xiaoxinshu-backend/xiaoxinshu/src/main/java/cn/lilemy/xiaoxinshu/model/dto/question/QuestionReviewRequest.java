package cn.lilemy.xiaoxinshu.model.dto.question;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 题目审核请求体
 */
@Data
public class QuestionReviewRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1271520722704119750L;

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
