package cn.lilemy.xiaoxinshu.model.dto.question;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 批量删除题目请求体
 */
@Data
public class QuestionBatchDeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 5294394324912797141L;

    /**
     * 题目 id 列表
     */
    private List<Long> questionIdList;
}
