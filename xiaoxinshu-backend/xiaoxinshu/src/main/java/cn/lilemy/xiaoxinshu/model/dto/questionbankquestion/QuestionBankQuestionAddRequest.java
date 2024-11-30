package cn.lilemy.xiaoxinshu.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建题库题目关联请求
 */
@Data
public class QuestionBankQuestionAddRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7889990419396930165L;

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;

}
