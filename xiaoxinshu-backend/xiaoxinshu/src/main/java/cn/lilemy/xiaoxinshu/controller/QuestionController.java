package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.annotation.CrawlerDetection;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.question.*;
import cn.lilemy.xiaoxinshucommon.model.entity.Question;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshu.model.enums.ReviewStatusEnum;
import cn.lilemy.xiaoxinshu.model.vo.QuestionPersonalVO;
import cn.lilemy.xiaoxinshu.model.vo.QuestionVO;
import cn.lilemy.xiaoxinshu.service.QuestionService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "QuestionController")
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    // region 题目增删改查

    @Operation(summary = "创建题目")
    @PostMapping("/create")
    public BaseResponse<Long> createQuestion(@RequestBody QuestionCreateRequest questionCreateRequest) {
        ThrowUtils.throwIf(questionCreateRequest == null, ResultCode.PARAMS_ERROR);
        Long questionId = questionService.createQuestion(questionCreateRequest);
        return ResultUtils.success(questionId);
    }

    @Operation(summary = "创建题目（管理员）")
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        ThrowUtils.throwIf(questionAddRequest == null, ResultCode.PARAMS_ERROR);
        Long addQuestionId = questionService.addQuestion(questionAddRequest);
        return ResultUtils.success(addQuestionId);
    }

    @Operation(summary = "删除题目")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ResultCode.PARAMS_ERROR);
        Long deleteRequestId = deleteRequest.getId();
        ThrowUtils.throwIf(deleteRequestId <= 0, ResultCode.PARAMS_ERROR);
        boolean result = questionService.deleteQuestion(deleteRequestId);
        return ResultUtils.success(result);
    }

    @Operation(summary = "批量删除题目")
    @PostMapping("/delete/batch")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestionBatch(@RequestBody QuestionBatchDeleteRequest questionBatchDeleteRequest) {
        ThrowUtils.throwIf(questionBatchDeleteRequest == null, ResultCode.PARAMS_ERROR);
        boolean result = questionService.batchDeleteQuestions(questionBatchDeleteRequest.getQuestionIdList());
        return ResultUtils.success(result);
    }

    @Operation(summary = "编辑题目")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest) {
        ThrowUtils.throwIf(questionEditRequest == null, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(questionEditRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        boolean result = questionService.editQuestion(questionEditRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新题目")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        ThrowUtils.throwIf(questionUpdateRequest == null, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(questionUpdateRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        boolean result = questionService.updateQuestion(questionUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据 id 获取题目（封装类）")
    @GetMapping("/get/vo")
    @CrawlerDetection()
    public BaseResponse<QuestionVO> getQuestionVOById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ResultCode.NOT_FOUND_ERROR);
        Integer reviewStatus = question.getReviewStatus();
        Long userId = question.getUserId();
        // 如果题目未通过审核，则只能创建用户查看
        if (reviewStatus != ReviewStatusEnum.PASS.getValue()) {
            User loginUser = userService.getLoginUser();
            Long loginUserId = loginUser.getId();
            if (!userId.equals(loginUserId) && !userService.isAdmin()) {
                throw new BusinessException(ResultCode.NO_AUTH_ERROR);
            }
        }
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question));
    }

    @Operation(summary = "根据 id 获取个人题目（封装类）")
    @GetMapping("/get/my/vo")
    public BaseResponse<QuestionPersonalVO> getQuestionPersonalById(Long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        QuestionPersonalVO result = questionService.getQuestionPersonalById(id);
        return ResultUtils.success(result);
    }

    @Operation(summary = "分页获取题目列表（仅管理员可用）")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    @Operation(summary = "分页获取未审核题目列表")
    @PostMapping("/list/reviewing")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listReviewingQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        QueryWrapper<Question> queryWrapper = questionService.getQueryWrapper(questionQueryRequest);
        // 添加查询条件
        queryWrapper.eq("review_status", ReviewStatusEnum.REVIEWING.getValue());
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(questionPage);
    }

    @Operation(summary = "分页获取题目列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        // 查询数据库
        Page<QuestionVO> questionVOPage = questionService.listQuestionByPageByCache(questionQueryRequest);
        // 获取封装类
        return ResultUtils.success(questionVOPage);
    }

    @Operation(summary = "分页获取当前登录用户创建的题目列表")
    @PostMapping("/list/my/vo")
    public BaseResponse<Page<QuestionPersonalVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser();
        questionQueryRequest.setUserId(loginUser.getId());
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionPersonalPage(questionPage));
    }

    // endregion

    // region 审核题目
    @Operation(summary = "审核题目")
    @PostMapping("/review")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> reviewQuestion(@RequestBody QuestionReviewRequest questionReviewRequest) {
        ThrowUtils.throwIf(questionReviewRequest == null, ResultCode.PARAMS_ERROR);
        boolean result = questionService.reviewQuestion(questionReviewRequest);
        return ResultUtils.success(result);
    }
    // endregion
}
