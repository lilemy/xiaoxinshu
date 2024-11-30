package cn.lilemy.xiaoxinshu.model.vo;

import cn.lilemy.xiaoxinshu.model.entity.QuestionBank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 题库列表信息
 */
@Data
public class QuestionBankListVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6724286247504099947L;

    /**
     * 题库 id
     */
    private Long id;

    /**
     * 题库标题
     */
    private String title;

    /**
     * 题库描述
     */
    private String description;

    public static QuestionBankListVO objToVO(QuestionBank questionBank) {
        QuestionBankListVO questionBankListVO = new QuestionBankListVO();
        questionBankListVO.setId(questionBank.getId());
        questionBankListVO.setTitle(questionBank.getTitle());
        questionBankListVO.setDescription(questionBank.getDescription());
        return questionBankListVO;
    }
}
