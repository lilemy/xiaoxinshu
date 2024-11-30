package cn.lilemy.xiaoxinshu.model.dto.questionbank;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建题库请求
 */
@Data
public class QuestionBankAddRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 5735236525005914358L;

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
}
