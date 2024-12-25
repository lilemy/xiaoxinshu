package cn.lilemy.xiaoxinshu.model.dto.question;

import lombok.Data;

@Data
public class QuestionViewNumRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 浏览量
     */
    private Integer viewNum;
}
