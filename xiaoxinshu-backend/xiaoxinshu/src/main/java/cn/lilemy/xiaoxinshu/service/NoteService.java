package cn.lilemy.xiaoxinshu.service;

import cn.lilemy.xiaoxinshu.model.dto.note.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lilemy.xiaoxinshucommon.model.entity.Note;
import cn.lilemy.xiaoxinshu.model.vo.NotePersonalVO;
import cn.lilemy.xiaoxinshu.model.vo.NoteVO;

/**
 * @author qq233
 * @description 针对表【note(笔记)】的数据库操作Service
 */
public interface NoteService extends IService<Note> {

    /**
     * 校验数据
     *
     * @param note 笔记信息
     * @param add  对创建的数据进行校验
     */
    void validQuestion(Note note, boolean add);

    /**
     * 创建笔记
     *
     * @param noteCreateRequest 笔记创建请求体
     * @return 笔记 id
     */
    Long createNote(NoteCreateRequest noteCreateRequest);

    /**
     * 删除笔记
     *
     * @param noteId 笔记 id
     * @return 是否操作成功
     */
    Boolean deleteNote(Long noteId);

    /**
     * 编辑笔记
     *
     * @param noteEditRequest 编辑笔记请求体
     * @return 是否编辑成功
     */
    Boolean editNote(NoteEditRequest noteEditRequest);

    /**
     * 更新笔记
     *
     * @param noteUpdateRequest 更新笔记请求体
     * @return 是否更新成功
     */
    Boolean updateNote(NoteUpdateRequest noteUpdateRequest);

    /**
     * 获取笔记封装
     *
     * @param note 笔记信息
     * @return 笔记信息封装
     */
    NoteVO getNoteVO(Note note);

    /**
     * 根据 id 获取个人笔记
     *
     * @param id 笔记 id
     * @return 个人笔记信息
     */
    NotePersonalVO getNotePersonalById(Long id);

    /**
     * 构建笔记查询条件
     *
     * @param noteQueryRequest 笔记查询请求体
     * @return 笔记查询条件
     */
    QueryWrapper<Note> getQueryWrapper(NoteQueryRequest noteQueryRequest);

    /**
     * 构建笔记查询条件（已审核笔记）
     *
     * @param noteQueryRequest 笔记查询请求体
     * @return 笔记查询条件
     */
    QueryWrapper<Note> getReviewQueryWrapper(NoteQueryRequest noteQueryRequest);

    /**
     * 根据分类 id 获取笔记分页列表
     *
     * @param noteQueryByCategoriesRequest 根据分类 id 获取笔记分页请求体
     * @return 笔记分页
     */
    Page<Note> getNotePageByCategoriesId(NoteQueryByCategoriesRequest noteQueryByCategoriesRequest);

    /**
     * 分页获取笔记信息封装
     *
     * @param notePage 笔记信息分页
     * @return 笔记封装信息分页
     */
    Page<NoteVO> getNoteVOPage(Page<Note> notePage);

    /**
     * 获取个人笔记信息封装
     *
     * @param notePage 笔记信息分页
     * @return 个人笔记信息分页
     */
    Page<NotePersonalVO> getNotePersonalPageByPage(Page<Note> notePage);

    /**
     * 笔记审核
     *
     * @param noteReviewRequest 笔记审核请求体
     * @return 是否操作成功
     */
    boolean reviewNote(NoteReviewRequest noteReviewRequest);

}
