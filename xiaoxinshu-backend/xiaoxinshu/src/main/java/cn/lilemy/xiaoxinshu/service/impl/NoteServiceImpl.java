package cn.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.lilemy.xiaoxinshu.model.dto.note.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lilemy.xiaoxinshu.common.ResultCode;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.exception.BusinessException;
import cn.lilemy.xiaoxinshu.exception.ThrowUtils;
import cn.lilemy.xiaoxinshu.mapper.NoteMapper;
import cn.lilemy.xiaoxinshu.model.dto.note.*;
import cn.lilemy.xiaoxinshu.model.entity.Categories;
import cn.lilemy.xiaoxinshu.model.entity.Note;
import cn.lilemy.xiaoxinshu.model.entity.NoteCategories;
import cn.lilemy.xiaoxinshu.model.entity.User;
import cn.lilemy.xiaoxinshu.model.enums.ReviewStatusEnum;
import cn.lilemy.xiaoxinshu.model.enums.VisibleStatusEnum;
import cn.lilemy.xiaoxinshu.model.vo.CategoriesVO;
import cn.lilemy.xiaoxinshu.model.vo.NotePersonalVO;
import cn.lilemy.xiaoxinshu.model.vo.NoteVO;
import cn.lilemy.xiaoxinshu.model.vo.UserVO;
import cn.lilemy.xiaoxinshu.service.CategoriesService;
import cn.lilemy.xiaoxinshu.service.NoteCategoriesService;
import cn.lilemy.xiaoxinshu.service.NoteService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshu.util.SqlUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qq233
 * @description 针对表【note(笔记)】的数据库操作Service实现
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note>
        implements NoteService {

    @Resource
    private UserService userService;

    @Resource
    private CategoriesService categoriesService;

    @Resource
    private NoteCategoriesService noteCategoriesService;

    @Override
    public void validQuestion(Note note, boolean add) {
        ThrowUtils.throwIf(note == null, ResultCode.PARAMS_ERROR);
        // 从对象中取值
        String title = note.getTitle();
        String content = note.getContent();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ResultCode.PARAMS_ERROR, "标题不能为空");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ResultCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content)) {
            ThrowUtils.throwIf(content.length() > 10240, ResultCode.PARAMS_ERROR, "内容过长");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNote(NoteCreateRequest noteCreateRequest) {
        Note note = new Note();
        BeanUtils.copyProperties(noteCreateRequest, note);
        // 填充默认值
        User loginUser = userService.getLoginUser();
        note.setUserId(loginUser.getId());
        List<String> tags = noteCreateRequest.getTags();
        if (tags != null) {
            note.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        this.validQuestion(note, true);
        // 写入数据库
        boolean result = this.save(note);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        List<Long> categoriesList = noteCreateRequest.getCategoriesIds();
        if (CollUtil.isNotEmpty(categoriesList)) {
            // 查询对应分类是否存在
            LambdaQueryWrapper<Categories> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Categories::getId, categoriesList);
            long count = categoriesService.count(queryWrapper);
            // 如果数据库中的记录数少于 List 的大小，则抛出异常
            ThrowUtils.throwIf(count < categoriesList.size(), ResultCode.PARAMS_ERROR, "存在一个或多个分类不存在");
            // 对应分类存在，则创建笔记分类关系
            List<NoteCategories> noteCategoriesList = categoriesList.stream()
                    .map(id -> {
                        NoteCategories noteCategories = new NoteCategories();
                        noteCategories.setNoteId(note.getId());
                        noteCategories.setCategoriesId(id);
                        noteCategories.setUserId(loginUser.getId());
                        return noteCategories;
                    })
                    .collect(Collectors.toList());
            boolean saveBatch = noteCategoriesService.saveBatch(noteCategoriesList);
            ThrowUtils.throwIf(!saveBatch, ResultCode.OPERATION_ERROR);
        }
        return note.getId();
    }

    @Override
    public Boolean deleteNote(Long noteId) {
        // 判断是否存在
        Note note = this.getById(noteId);
        ThrowUtils.throwIf(note == null, ResultCode.NOT_FOUND_ERROR);
        User loginUser = userService.getLoginUser();
        // 仅本人或管理员可删除
        if (!note.getUserId().equals(loginUser.getId()) && !userService.isAdmin()) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(noteId);
        // todo 添加关联删除（删除笔记分类关联信息）
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editNote(NoteEditRequest noteEditRequest) {
        // 判断是否存在
        Long noteId = noteEditRequest.getId();
        Note oldNote = this.getById(noteId);
        ThrowUtils.throwIf(oldNote == null, ResultCode.NOT_FOUND_ERROR);
        // 仅本人可编辑
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(!oldNote.getUserId().equals(loginUser.getId()), ResultCode.NO_AUTH_ERROR);
        // 校验数据
        Note note = new Note();
        BeanUtils.copyProperties(noteEditRequest, note);
        this.validQuestion(note, false);
        // 添加更新条件
        LambdaUpdateChainWrapper<Note> noteUpdateWrapper = this.lambdaUpdate();
        noteUpdateWrapper.eq(Note::getId, noteId);
        String title = noteEditRequest.getTitle();
        String content = noteEditRequest.getContent();
        String picture = noteEditRequest.getPicture();
        Integer visible = noteEditRequest.getVisible();
        Integer isTop = noteEditRequest.getIsTop();
        noteUpdateWrapper.set(StringUtils.isNotBlank(title), Note::getTitle, title);
        noteUpdateWrapper.set(StringUtils.isNotBlank(content), Note::getContent, content);
        noteUpdateWrapper.set(StringUtils.isNotBlank(picture), Note::getPicture, picture);
        noteUpdateWrapper.set(visible != null, Note::getVisible, visible);
        noteUpdateWrapper.set(isTop != null, Note::getIsTop, isTop);
        // 设置笔记标签
        List<String> tags = noteEditRequest.getTags();
        if (tags != null) {
            noteUpdateWrapper.set(Note::getTags, JSONUtil.toJsonStr(tags));
        }
        // 设置笔记分类关联
        List<Long> categoriesList = noteEditRequest.getCategoriesList();
        // 获取笔记关联的分类 id
        LambdaQueryWrapper<NoteCategories> eq = Wrappers.lambdaQuery(NoteCategories.class)
                .select(NoteCategories::getCategoriesId)
                .eq(NoteCategories::getNoteId, noteId);
        List<Long> categoriesIds = noteCategoriesService.listObjs(eq, obj -> (Long) obj);
        // 获取需要添加的笔记分类关联
        List<Long> addIdList = CollUtil.isNotEmpty(categoriesList)
                ? new ArrayList<>(categoriesList)
                : new ArrayList<>();
        addIdList.removeAll(categoriesIds);
        if (CollUtil.isNotEmpty(addIdList)) {
            List<NoteCategories> noteCategoriesList = addIdList.stream().map(categoriesId -> {
                NoteCategories noteCategories = new NoteCategories();
                noteCategories.setNoteId(noteId);
                noteCategories.setCategoriesId(categoriesId);
                noteCategories.setUserId(loginUser.getId());
                return noteCategories;
            }).toList();
            noteCategoriesService.saveBatch(noteCategoriesList);
        }
        // 获取需要删除的笔记分类关联
        List<Long> deleteIdList = CollUtil.isNotEmpty(categoriesIds)
                ? new ArrayList<>(categoriesIds)
                : new ArrayList<>();
        if (CollUtil.isNotEmpty(categoriesList)) {
            deleteIdList.removeAll(categoriesList);
        }
        if (CollUtil.isNotEmpty(deleteIdList)) {
            QueryWrapper<NoteCategories> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("note_id", noteId);
            queryWrapper.in("categories_id", deleteIdList);
            noteCategoriesService.remove(queryWrapper);
        }
        // 更新后需重新审核
        if (!oldNote.getReviewStatus().equals(ReviewStatusEnum.REVIEWING.getValue())) {
            noteUpdateWrapper
                    .set(Note::getReviewMessage, null)
                    .set(Note::getReviewStatus, ReviewStatusEnum.REVIEWING.getValue())
                    .set(Note::getReviewerId, null)
                    .set(Note::getReviewTime, null);
        }
        // 设置编辑时间
        noteUpdateWrapper.set(Note::getEditTime, new Date());
        // 操作数据库，更新笔记数据
        boolean update = noteUpdateWrapper.update();
        ThrowUtils.throwIf(!update, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public Boolean updateNote(NoteUpdateRequest noteUpdateRequest) {
        return null;
    }

    @Override
    public NoteVO getNoteVO(Note note) {
        // 对象转封装类
        NoteVO noteVO = NoteVO.objToVo(note);
        // 关联查询用户信息
        Long userId = note.getUserId();
        UserVO userVO = userService.getUserVO(userId);
        noteVO.setUser(userVO);
        return noteVO;
    }

    @Override
    public NotePersonalVO getNotePersonalById(Long id) {
        // 查询数据库
        Note note = this.getById(id);
        ThrowUtils.throwIf(note == null, ResultCode.NOT_FOUND_ERROR);
        // 判断是否为创建用户
        Long loginUserId = userService.getLoginUser().getId();
        ThrowUtils.throwIf(!note.getUserId().equals(loginUserId), ResultCode.NO_AUTH_ERROR);
        NotePersonalVO notePersonalVO = NotePersonalVO.objToVo(note);
        LambdaQueryWrapper<NoteCategories> eq = Wrappers.lambdaQuery(NoteCategories.class)
                .select(NoteCategories::getCategoriesId)
                .eq(NoteCategories::getNoteId, id);
        List<Long> categoriesIdList = noteCategoriesService.listObjs(eq, obj -> (Long) obj);
        if (CollUtil.isNotEmpty(categoriesIdList)) {
            QueryWrapper<Categories> categoriesQueryWrapper = new QueryWrapper<>();
            categoriesQueryWrapper.in("id", categoriesIdList);
            categoriesQueryWrapper.select("id", "name", "priority", "create_time", "update_time");
            List<CategoriesVO> categoriesVOList = categoriesService.list(categoriesQueryWrapper)
                    .stream().map(CategoriesVO::objToVo).toList();
            notePersonalVO.setCategoriesVOList(categoriesVOList);
        }
        return notePersonalVO;
    }

    @Override
    public QueryWrapper<Note> getQueryWrapper(NoteQueryRequest noteQueryRequest) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        if (noteQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = noteQueryRequest.getId();
        Long notId = noteQueryRequest.getNotId();
        String searchText = noteQueryRequest.getSearchText();
        String title = noteQueryRequest.getTitle();
        String content = noteQueryRequest.getContent();
        List<String> tagList = noteQueryRequest.getTags();
        Long userId = noteQueryRequest.getUserId();
        Integer visible = noteQueryRequest.getVisible();
        boolean isNeedContent = noteQueryRequest.isNeedContent();
        String sortField = noteQueryRequest.getSortField();
        String sortOrder = noteQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 是否需要笔记内容
        if (!isNeedContent) {
            queryWrapper.select("id", "title", "tags", "user_id", "picture",
                    "review_status", "review_message", "review_time", "reviewer_id",
                    "view_num", "thumb_num", "favour_num", "visible", "is_top",
                    "edit_time", "create_time", "update_time", "is_delete");
        }
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(visible), "visible", visible);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public QueryWrapper<Note> getReviewQueryWrapper(NoteQueryRequest noteQueryRequest) {
        QueryWrapper<Note> queryWrapper = this.getQueryWrapper(noteQueryRequest);
        queryWrapper.eq("visible", VisibleStatusEnum.OPEN.getValue());
        queryWrapper.eq("review_status", ReviewStatusEnum.PASS.getValue());
        return queryWrapper;
    }

    @Override
    public Page<Note> getNotePageByCategoriesId(NoteQueryByCategoriesRequest noteQueryByCategoriesRequest) {
        Long categoriesId = noteQueryByCategoriesRequest.getCategoriesId();
        ThrowUtils.throwIf(categoriesId <= 0, ResultCode.PARAMS_ERROR);
        LambdaQueryWrapper<NoteCategories> lambdaQueryWrapper = Wrappers.lambdaQuery(NoteCategories.class)
                .select(NoteCategories::getNoteId)
                .eq(NoteCategories::getCategoriesId, categoriesId);
        List<Long> noteIdList = noteCategoriesService.listObjs(lambdaQueryWrapper, obj -> (Long) obj);
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        // 获取已审核的笔记
        queryWrapper.eq("review_status", ReviewStatusEnum.PASS.getValue());
        // 获取公开的笔记
        queryWrapper.eq("visible", VisibleStatusEnum.OPEN.getValue());
        int current = noteQueryByCategoriesRequest.getCurrent();
        int pageSize = noteQueryByCategoriesRequest.getPageSize();
        String sortField = noteQueryByCategoriesRequest.getSortField();
        String sortOrder = noteQueryByCategoriesRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        if (CollUtil.isEmpty(noteIdList)) {
            return new Page<>(current, pageSize);
        }
        queryWrapper.in("id", noteIdList);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        // 查询数据库
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    @Override
    public Page<NoteVO> getNoteVOPage(Page<Note> notePage) {
        List<Note> noteList = notePage.getRecords();
        Page<NoteVO> noteVOPage = new Page<>(notePage.getCurrent(), notePage.getSize(), notePage.getTotal());
        if (CollUtil.isEmpty(noteList)) {
            return noteVOPage;
        }
        // 对象列表 => 封装对象列表
        List<NoteVO> noteVOList = noteList.stream().map(NoteVO::objToVo).collect(Collectors.toList());
        // 关联查询用户信息
        Set<Long> userIdSet = noteList.stream().map(Note::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        noteVOList.forEach(noteVO -> {
            Long userId = noteVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            noteVO.setUser(userService.getUserVO(user));
        });
        noteVOPage.setRecords(noteVOList);
        return noteVOPage;
    }

    @Override
    public Page<NotePersonalVO> getNotePersonalPageByPage(Page<Note> notePage) {
        List<Note> noteList = notePage.getRecords();
        Page<NotePersonalVO> notePersonalVOPage = new Page<>(notePage.getCurrent(), notePage.getSize(), notePage.getTotal());
        if (CollUtil.isEmpty(noteList)) {
            return notePersonalVOPage;
        }
        // 对象列表转封装对象列表
        List<NotePersonalVO> notePersonalList = noteList.stream().map(NotePersonalVO::objToVo).toList();
        // 关联查询分类信息
        Map<Long, List<CategoriesVO>> noteCategoriesListVO = noteList.stream().collect(Collectors.toMap(
                Note::getId,
                note -> {
                    Long noteId = note.getId();
                    LambdaQueryWrapper<NoteCategories> eq = Wrappers.lambdaQuery(NoteCategories.class)
                            .select(NoteCategories::getCategoriesId)
                            .eq(NoteCategories::getNoteId, noteId);
                    // 获取所属分类列表
                    List<Long> noteCategoriesIdList = noteCategoriesService.listObjs(eq, obj -> (Long) obj);
                    if (CollUtil.isNotEmpty(noteCategoriesIdList)) {
                        QueryWrapper<Categories> categoriesQueryWrapper = new QueryWrapper<>();
                        categoriesQueryWrapper.in("id", noteCategoriesIdList);
                        categoriesQueryWrapper.select("id", "name", "priority", "create_time", "update_time");
                        List<Categories> categoriesList = categoriesService.list(categoriesQueryWrapper);
                        return categoriesList.stream()
                                .map(CategoriesVO::objToVo)
                                .collect(Collectors.toList());
                    }
                    return Collections.emptyList();
                }
        ));
        // 填充信息
        notePersonalList.forEach(notePersonalVO -> {
            Long noteId = notePersonalVO.getId();
            List<CategoriesVO> categoriesVOList = new ArrayList<>();
            if (noteCategoriesListVO.containsKey(noteId)) {
                categoriesVOList = noteCategoriesListVO.get(noteId);
            }
            notePersonalVO.setCategoriesVOList(categoriesVOList);
        });
        notePersonalVOPage.setRecords(notePersonalList);
        return notePersonalVOPage;
    }

    @Override
    public boolean reviewNote(NoteReviewRequest noteReviewRequest) {
        // 查询审核题目信息
        Long noteId = noteReviewRequest.getId();
        ThrowUtils.throwIf(noteId <= 0, ResultCode.PARAMS_ERROR);
        Note note = this.getById(noteId);
        ThrowUtils.throwIf(note == null, ResultCode.NOT_FOUND_ERROR, "笔记不存在");
        // 获取审核信息
        Integer reviewStatus = noteReviewRequest.getReviewStatus();
        String reviewMessage = noteReviewRequest.getReviewMessage();
        // 如果审核信息为空，则添加默认信息
        if (StringUtils.isBlank(reviewMessage)) {
            // 如果审核通过
            if (reviewStatus == ReviewStatusEnum.PASS.getValue()) {
                reviewMessage = ReviewStatusEnum.PASS.getText();
            }
            // 如果审核不通过
            if (reviewStatus == ReviewStatusEnum.REJECT.getValue()) {
                reviewMessage = ReviewStatusEnum.REJECT.getText();
            }
        }
        // 获取审核人信息
        User loginUser = userService.getLoginUser();
        Long userId = loginUser.getId();
        // 获取审核时间
        note.setReviewTime(new Date());
        note.setReviewStatus(reviewStatus);
        note.setReviewMessage(reviewMessage);
        note.setReviewerId(userId);
        boolean result = this.updateById(note);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }
}
