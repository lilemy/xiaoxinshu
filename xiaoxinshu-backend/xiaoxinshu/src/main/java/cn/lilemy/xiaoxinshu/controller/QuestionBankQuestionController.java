package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.questionbankquestion.*;
import cn.lilemy.xiaoxinshucommon.model.entity.QuestionBankQuestion;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshu.model.vo.QuestionBankQuestionVO;
import cn.lilemy.xiaoxinshu.service.QuestionBankQuestionService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "QuestionBankQuestionController")
@RequestMapping("/questionBankQuestion")
public class QuestionBankQuestionController {

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    @Resource
    private UserService userService;

    @Operation(summary = "创建题库题目关联")
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestionBankQuestion(@RequestBody QuestionBankQuestionAddRequest questionBankQuestionAddRequest) {
        ThrowUtils.throwIf(questionBankQuestionAddRequest == null, ResultCode.PARAMS_ERROR);
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(questionBankQuestionAddRequest, questionBankQuestion);
        // 参数校验
        questionBankQuestionService.validQuestionBankQuestion(questionBankQuestion, true);
        // 填充默认值
        User loginUser = userService.getLoginUser();
        questionBankQuestion.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionBankQuestionService.save(questionBankQuestion);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionBankQuestionId = questionBankQuestion.getId();
        return ResultUtils.success(newQuestionBankQuestionId);
    }

    @Operation(summary = "移除题库题目关联")
    @PostMapping("/remove")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> removeQuestionBankQuestion(
            @RequestBody QuestionBankQuestionRemoveRequest questionBankQuestionRemoveRequest
    ) {
        // 参数校验
        ThrowUtils.throwIf(questionBankQuestionRemoveRequest == null, ResultCode.PARAMS_ERROR);
        Long questionBankId = questionBankQuestionRemoveRequest.getQuestionBankId();
        Long questionId = questionBankQuestionRemoveRequest.getQuestionId();
        ThrowUtils.throwIf(questionBankId == null || questionId == null, ResultCode.PARAMS_ERROR);
        // 构造查询
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .eq(QuestionBankQuestion::getQuestionId, questionId);
        boolean result = questionBankQuestionService.remove(lambdaQueryWrapper);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新题库题目关联")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBankQuestion(@RequestBody QuestionBankQuestionUpdateRequest questionBankQuestionUpdateRequest) {
        // 参数校验
        ThrowUtils.throwIf(questionBankQuestionUpdateRequest == null, ResultCode.PARAMS_ERROR);
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(questionBankQuestionUpdateRequest, questionBankQuestion);
        questionBankQuestionService.validQuestionBankQuestion(questionBankQuestion, false);
        // 写入数据库
        boolean result = questionBankQuestionService.updateById(questionBankQuestion);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据 id 获取题库题目关联（封装类）")
    @GetMapping("/get/vo")
    public BaseResponse<QuestionBankQuestionVO> getQuestionBankQuestionVOById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        QuestionBankQuestion questionBankQuestion = questionBankQuestionService.getById(id);
        ThrowUtils.throwIf(questionBankQuestion == null, ResultCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionBankQuestionService.getQuestionBankQuestionVO(questionBankQuestion));
    }

    @Operation(summary = "分页获取题库题目关联列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<QuestionBankQuestionVO>> listQuestionBankQuestionVOByPage(@RequestBody QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        long current = questionBankQuestionQueryRequest.getCurrent();
        long size = questionBankQuestionQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionBankQuestion> questionBankQuestionPage = questionBankQuestionService.page(new Page<>(current, size),
                questionBankQuestionService.getQueryWrapper(questionBankQuestionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBankQuestionService.getQuestionBankQuestionVOPage(questionBankQuestionPage));
    }

    @Operation(summary = "批量添加题目到题库")
    @PostMapping("/add/batch")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> batchAddQuestionsToBank(@RequestBody QuestionBankQuestionBatchRequest questionBankQuestionBatchAddRequest) {
        ThrowUtils.throwIf(questionBankQuestionBatchAddRequest == null, ResultCode.PARAMS_ERROR);
        questionBankQuestionService.batchAddQuestionsToBank(questionBankQuestionBatchAddRequest);
        return ResultUtils.success(true);
    }

    @Operation(summary = "批量从题库移除题目")
    @PostMapping("remove/batch")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> batchRemoveQuestionsFromBank(@RequestBody QuestionBankQuestionBatchRequest questionBankQuestionBatchRemoveRequest) {
        ThrowUtils.throwIf(questionBankQuestionBatchRemoveRequest == null, ResultCode.PARAMS_ERROR);
        questionBankQuestionService.batchRemoveQuestionsFromBank(questionBankQuestionBatchRemoveRequest);
        return ResultUtils.success(true);
    }
}
