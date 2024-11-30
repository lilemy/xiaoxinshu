package cn.lilemy.xiaoxinshu.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 题库题目关联批量操作请求体
 */
@Data
public class QuestionBankQuestionBatchRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 9209718813758074198L;

    /**
     * 题库 id
     */
    private Long questionBankId;
    /**
     * 题目 id 列表
     */
    private List<Long> questionIdList;

}
