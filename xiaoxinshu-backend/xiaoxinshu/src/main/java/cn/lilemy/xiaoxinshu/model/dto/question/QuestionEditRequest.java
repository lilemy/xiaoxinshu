package cn.lilemy.xiaoxinshu.model.dto.question;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 编辑题目请求
 */
@Data
public class QuestionEditRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -6363940361886504553L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 题库 id 列表
     */
    private List<Long> questionBankList;

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
