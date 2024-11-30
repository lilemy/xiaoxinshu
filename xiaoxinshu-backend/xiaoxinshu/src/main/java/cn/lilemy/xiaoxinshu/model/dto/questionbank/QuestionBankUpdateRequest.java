package cn.lilemy.xiaoxinshu.model.dto.questionbank;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新题库请求
 */
@Data
public class QuestionBankUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 3347449562443270102L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

    /**
     * 优先级
     */
    private Integer priority;
}
