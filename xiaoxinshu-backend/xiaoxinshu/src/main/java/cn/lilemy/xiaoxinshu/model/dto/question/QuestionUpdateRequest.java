package cn.lilemy.xiaoxinshu.model.dto.question;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 更新题目请求（管理员）
 */
@Data
public class QuestionUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 4175368242992643445L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 推荐答案
     */
    private String answer;


    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

}
