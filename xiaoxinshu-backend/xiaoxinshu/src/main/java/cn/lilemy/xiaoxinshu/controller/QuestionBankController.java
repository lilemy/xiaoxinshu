package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.question.QuestionQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.questionbank.QuestionBankAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.questionbank.QuestionBankQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.questionbank.QuestionBankUpdateRequest;
import cn.lilemy.xiaoxinshu.model.entity.Question;
import cn.lilemy.xiaoxinshu.model.entity.QuestionBank;
import cn.lilemy.xiaoxinshu.model.vo.QuestionBankListVO;
import cn.lilemy.xiaoxinshu.model.vo.QuestionBankVO;
import cn.lilemy.xiaoxinshu.model.vo.QuestionVO;
import cn.lilemy.xiaoxinshu.service.QuestionBankService;
import cn.lilemy.xiaoxinshu.service.QuestionService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "QuestionBankController")
@RequestMapping("/questionBank")
public class QuestionBankController {

    @Resource
    private QuestionBankService questionBankService;

    @Resource
    private QuestionService questionService;

    // region 增删改查

    @Operation(summary = "添加题库")
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestionBank(@RequestBody QuestionBankAddRequest questionBankAddRequest) {
        ThrowUtils.throwIf(questionBankAddRequest == null, ResultCode.PARAMS_ERROR);
        Long addQuestionBankId = questionBankService.addQuestionBank(questionBankAddRequest);
        return ResultUtils.success(addQuestionBankId);
    }

    @Operation(summary = "删除题库")
    @PostMapping("/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestionBank(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ResultCode.PARAMS_ERROR);
        Long questionBankId = deleteRequest.getId();
        ThrowUtils.throwIf(questionBankId <= 0, ResultCode.PARAMS_ERROR);
        Boolean result = questionBankService.deleteQuestionBank(questionBankId);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新题库")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBank(@RequestBody QuestionBankUpdateRequest questionBankUpdateRequest) {
        ThrowUtils.throwIf(questionBankUpdateRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = questionBankService.updateQuestionBank(questionBankUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据id获取题库脱敏信息")
    @GetMapping("/get/vo")
    public BaseResponse<QuestionBankVO> getQuestionBankVOById(Long id, Boolean isNeedQueryQuestionList) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        // 生成 key
//        String key = "bank_detail_" + id;
        // 如果是热 key
//        if (JdHotKeyStore.isHotKey(key)) {
//            // 从本地缓存中获取缓存值
//            Object cacheQuestionBankVO = JdHotKeyStore.get(key);
//            if (cacheQuestionBankVO != null) {
//                // 如果缓存中有值，直接返回缓存值
//                return ResultUtils.success((QuestionBankVO) cacheQuestionBankVO);
//            }
//        }
        // 查询数据库
        QuestionBank questionBank = questionBankService.getById(id);
        ThrowUtils.throwIf(questionBank == null, ResultCode.NOT_FOUND_ERROR);
        QuestionBankVO questionBankVO = questionBankService.getQuestionBankVO(questionBank);
        // 是否要关联查询题库下的题目列表
        if (isNeedQueryQuestionList) {
            QuestionQueryRequest questionQueryRequest = new QuestionQueryRequest();
            questionQueryRequest.setQuestionBankId(id);
            Page<Question> questionPage = questionService.listQuestionByPage(questionQueryRequest, false);
            Page<QuestionVO> questionVOPage = questionService.getQuestionVOPage(questionPage);
            questionBankVO.setQuestionPage(questionVOPage);
        }
        // 设置本地缓存
//        JdHotKeyStore.smartSet(key, questionBankVO);
        // 获取封装类
        return ResultUtils.success(questionBankVO);
    }

    @Operation(summary = "获取题库列表信息")
    @GetMapping("/get/list")
    public BaseResponse<List<QuestionBankListVO>> getQuestionBankList() {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "description");
        List<QuestionBank> list = questionBankService.list(queryWrapper);
        List<QuestionBankListVO> questionBankListVOList = list.stream().map(QuestionBankListVO::objToVO).toList();
        return ResultUtils.success(questionBankListVOList);
    }

    @Operation(summary = "分页获取题库列表（仅管理员可用）")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionBank>> listQuestionBankByPage(@RequestBody QuestionBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionBank> questionBankPage = questionBankService.page(new Page<>(current, size),
                questionBankService.getQueryWrapper(questionBankQueryRequest));
        return ResultUtils.success(questionBankPage);
    }

    @Operation(summary = "分页获取题库列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<QuestionBankVO>> listQuestionBankVOByPage(@RequestBody QuestionBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 200, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBank> questionBankPage = questionBankService.page(new Page<>(current, size),
                questionBankService.getQueryWrapper(questionBankQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBankService.getQuestionBankVOPage(questionBankPage));
    }

    // endregion

}
