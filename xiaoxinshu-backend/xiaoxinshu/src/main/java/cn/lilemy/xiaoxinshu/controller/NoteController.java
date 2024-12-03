package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.note.*;
import cn.lilemy.xiaoxinshu.model.entity.Note;
import cn.lilemy.xiaoxinshu.model.entity.User;
import cn.lilemy.xiaoxinshu.model.enums.ReviewStatusEnum;
import cn.lilemy.xiaoxinshu.model.enums.VisibleStatusEnum;
import cn.lilemy.xiaoxinshu.model.vo.NotePersonalVO;
import cn.lilemy.xiaoxinshu.model.vo.NoteVO;
import cn.lilemy.xiaoxinshu.service.NoteService;
import cn.lilemy.xiaoxinshu.service.UserService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 笔记接口
 */
@Slf4j
@RestController
@RequestMapping("/note")
@Tag(name = "NoteController")
public class NoteController {

    @Resource
    private NoteService noteService;

    @Resource
    private UserService userService;

    // region 增删改查

    @Operation(summary = "创建笔记")
    @PostMapping("/create")
    public BaseResponse<Long> createNote(@RequestBody NoteCreateRequest noteCreateRequest) {
        ThrowUtils.throwIf(noteCreateRequest == null, ResultCode.PARAMS_ERROR);
        Long noteId = noteService.createNote(noteCreateRequest);
        return ResultUtils.success(noteId);
    }

    @Operation(summary = "添加笔记（管理员）")
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addNote(@RequestBody NoteAddRequest noteAddRequest) {
        ThrowUtils.throwIf(noteAddRequest == null, ResultCode.PARAMS_ERROR);
        NoteCreateRequest noteCreateRequest = new NoteCreateRequest();
        BeanUtils.copyProperties(noteAddRequest, noteCreateRequest);
        Long note = noteService.createNote(noteCreateRequest);
        return ResultUtils.success(note);
    }

    @Operation(summary = "删除笔记")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNote(@RequestBody DeleteRequest deleteRequest) {
        Long noteId = deleteRequest.getId();
        ThrowUtils.throwIf(noteId == null || noteId <= 0, ResultCode.PARAMS_ERROR);
        Boolean result = noteService.deleteNote(noteId);
        return ResultUtils.success(result);
    }

    @Operation(summary = "编辑笔记")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editNote(@RequestBody NoteEditRequest noteEditRequest) {
        ThrowUtils.throwIf(noteEditRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = noteService.editNote(noteEditRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新笔记")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNote(@RequestBody NoteUpdateRequest noteUpdateRequest) {
        ThrowUtils.throwIf(noteUpdateRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = noteService.updateNote(noteUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据id获取笔记封装")
    @GetMapping("/get/vo")
    public BaseResponse<NoteVO> getNoteVOById(Long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Note note = noteService.getById(id);
        ThrowUtils.throwIf(note == null, ResultCode.NOT_FOUND_ERROR);
        // 判断笔记是否公开
        Integer visible = note.getVisible();
        ThrowUtils.throwIf(visible == null || visible != VisibleStatusEnum.OPEN.getValue(), ResultCode.NO_AUTH_ERROR);
        Integer reviewStatus = note.getReviewStatus();
        // 判断笔记是否通过审核
        ThrowUtils.throwIf(reviewStatus != ReviewStatusEnum.PASS.getValue(), ResultCode.NO_AUTH_ERROR);
        // 获取封装类
        return ResultUtils.success(noteService.getNoteVO(note));
    }

    @Operation(summary = "根据 id 获取个人笔记")
    @GetMapping("/get/my/vo")
    public BaseResponse<NotePersonalVO> getNotePersonalVOById(Long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        NotePersonalVO notePersonalVO = noteService.getNotePersonalById(id);
        return ResultUtils.success(notePersonalVO);
    }

    @Operation(summary = "分页获取笔记列表（仅管理员可用）")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Note>> listNoteByPage(@RequestBody NoteQueryRequest noteQueryRequest) {
        ThrowUtils.throwIf(noteQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = noteQueryRequest.getCurrent();
        long size = noteQueryRequest.getPageSize();
        // 查询数据库
        Page<Note> notePage = noteService.page(new Page<>(current, size),
                noteService.getQueryWrapper(noteQueryRequest));
        return ResultUtils.success(notePage);
    }

    @Operation(summary = "分页获取未审核笔记列表")
    @PostMapping("/list/reviewing")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Note>> listReviewingNoteByPage(@RequestBody NoteQueryRequest noteQueryRequest) {
        ThrowUtils.throwIf(noteQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = noteQueryRequest.getCurrent();
        long size = noteQueryRequest.getPageSize();
        QueryWrapper<Note> queryWrapper = noteService.getQueryWrapper(noteQueryRequest);
        // 添加查询条件
        queryWrapper.eq("review_status", ReviewStatusEnum.REVIEWING.getValue());
        // 查询数据库
        Page<Note> notePage = noteService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(notePage);
    }

    @Operation(summary = "分页获取笔记列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<NoteVO>> listNoteVOByPage(@RequestBody NoteQueryRequest noteQueryRequest) {
        ThrowUtils.throwIf(noteQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = noteQueryRequest.getCurrent();
        long size = noteQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Page<Note> notePage = noteService.page(new Page<>(current, size),
                noteService.getReviewQueryWrapper(noteQueryRequest));
        // 获取封装类
        return ResultUtils.success(noteService.getNoteVOPage(notePage));
    }

    @Operation(summary = "根据分类 id 分页获取笔记列表（封装类）")
    @PostMapping("/list/id/vo")
    public BaseResponse<Page<NoteVO>> listNoteVOByCategoriesId(@RequestBody NoteQueryByCategoriesRequest noteQueryByCategoriesRequest) {
        ThrowUtils.throwIf(noteQueryByCategoriesRequest == null, ResultCode.PARAMS_ERROR);
        Page<Note> notePage = noteService.getNotePageByCategoriesId(noteQueryByCategoriesRequest);
        // 获取封装类
        return ResultUtils.success(noteService.getNoteVOPage(notePage));
    }

    @Operation(summary = "分页获取个人笔记列表")
    @PostMapping("/list/my/vo")
    public BaseResponse<Page<NotePersonalVO>> listMyNoteVOByPage(@RequestBody NoteQueryRequest noteQueryRequest) {
        ThrowUtils.throwIf(noteQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = noteQueryRequest.getCurrent();
        long size = noteQueryRequest.getPageSize();
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        noteQueryRequest.setUserId(loginUser.getId());
        // 查询数据库
        Page<Note> notePage = noteService.page(new Page<>(current, size),
                noteService.getQueryWrapper(noteQueryRequest));
        // 获取封装类
        return ResultUtils.success(noteService.getNotePersonalPageByPage(notePage));
    }

    // endregion

    // region 审核笔记
    @Operation(summary = "审核笔记")
    @PostMapping("/review")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> reviewNote(@RequestBody NoteReviewRequest noteReviewRequest) {
        ThrowUtils.throwIf(noteReviewRequest == null, ResultCode.PARAMS_ERROR);
        boolean result = noteService.reviewNote(noteReviewRequest);
        return ResultUtils.success(result);
    }
    // endregion
}
