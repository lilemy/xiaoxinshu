package cn.lilemy.xiaoxinshu.model.dto.question;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建题目请求体
 */
@Data
public class QuestionCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8074531257717820850L;

    /**
     * 标题
     */
    private String title;

    /**
     * 所属题库
     */
    private List<Long> questionBankIds;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 推荐答案
     */
    private String answer;
}
