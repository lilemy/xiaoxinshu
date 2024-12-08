package cn.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.lilemy.xiaoxinshu.model.dto.questionbank.QuestionBankAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.questionbank.QuestionBankQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.questionbank.QuestionBankUpdateRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.QuestionBank;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lilemy.xiaoxinshu.model.vo.QuestionBankVO;

/**
 * @author qq233
 * @description 针对表【question_bank(题库)】的数据库操作Service
 */
public interface QuestionBankService extends IService<QuestionBank> {

    /**
     * 校验数据
     *
     * @param questionBank 题库
     * @param add          对创建的数据进行校验
     */
    void validQuestionBank(QuestionBank questionBank, boolean add);

    /**
     * 添加题库
     *
     * @param questionBankAddRequest 题库创建请求体
     * @return 新建题库 id
     */
    Long addQuestionBank(QuestionBankAddRequest questionBankAddRequest);

    /**
     * 删除题库
     *
     * @param id 题库 id
     * @return 是否成功删除
     */
    Boolean deleteQuestionBank(Long id);

    /**
     * 更新题库
     *
     * @param questionBankUpdateRequest 题库更新请求体
     * @return 是否更新成功
     */
    Boolean updateQuestionBank(QuestionBankUpdateRequest questionBankUpdateRequest);

    /**
     * 获取查询条件
     *
     * @param questionBankQueryRequest 题库查询条件
     * @return 题库查询
     */
    QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest);

    /**
     * 获取题库封装
     *
     * @param questionBank 题库
     * @return {@link QuestionBankVO}
     */
    QuestionBankVO getQuestionBankVO(QuestionBank questionBank);

    /**
     * 分页获取题库封装
     *
     * @param questionBankPage 题库分页
     * @return {@link Page<QuestionBankVO>}
     */
    Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage);

}
