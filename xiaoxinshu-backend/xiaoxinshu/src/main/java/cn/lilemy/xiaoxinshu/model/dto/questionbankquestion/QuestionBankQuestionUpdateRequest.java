package cn.lilemy.xiaoxinshu.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新题库题目关联请求
 */
@Data
public class QuestionBankQuestionUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -2467501468075133476L;

    /**
     * id
     */
    private Long id;

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;
}
