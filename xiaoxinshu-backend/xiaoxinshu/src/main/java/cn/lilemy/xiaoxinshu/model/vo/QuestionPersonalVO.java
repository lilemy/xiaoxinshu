package cn.lilemy.xiaoxinshu.model.vo;

import cn.hutool.json.JSONUtil;
import cn.lilemy.xiaoxinshu.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 个人题目封装
 */
@Data
public class QuestionPersonalVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1032914683193683084L;

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
     * 推荐答案
     */
    private String answer;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 所属题库
     */
    private List<QuestionBankListVO> questionBankList;

    /**
     * 对象转封装类
     *
     * @param question 题目信息
     * @return 题目脱敏信息
     */
    public static QuestionPersonalVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionPersonalVO questionPersonalVO = new QuestionPersonalVO();
        BeanUtils.copyProperties(question, questionPersonalVO);
        questionPersonalVO.setTagList(JSONUtil.toList(JSONUtil.parseArray(question.getTags()), String.class));
        return questionPersonalVO;
    }

}
