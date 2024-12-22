package cn.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.manager.upload.FilePictureUpload;
import cn.lilemy.xiaoxinshu.manager.upload.PictureUploadTemplate;
import cn.lilemy.xiaoxinshu.manager.upload.UrlPictureUpload;
import cn.lilemy.xiaoxinshu.mapper.PictureMapper;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadPictureResult;
import cn.lilemy.xiaoxinshu.model.dto.picture.*;
import cn.lilemy.xiaoxinshu.model.enums.ReviewStatusEnum;
import cn.lilemy.xiaoxinshu.model.vo.PictureVO;
import cn.lilemy.xiaoxinshu.model.vo.UserVO;
import cn.lilemy.xiaoxinshu.service.PictureService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshu.util.SqlUtils;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.Picture;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qq233
 * @description 针对表【picture(图片)】的数据库操作Service实现
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {

    @Resource
    private UserService userService;

    @Resource
    private FilePictureUpload filePictureUpload;

    @Resource
    private UrlPictureUpload urlPictureUpload;

    @Override
    public void validPicture(Picture picture) {
        ThrowUtils.throwIf(picture == null, ResultCode.PARAMS_ERROR);
        // 从对象中取值  
        Long id = picture.getId();
        String url = picture.getUrl();
        String introduction = picture.getIntroduction();
        // 修改数据时，id 不能为空，有参数则校验  
        ThrowUtils.throwIf(ObjUtil.isNull(id), ResultCode.PARAMS_ERROR, "id 不能为空");
        if (StrUtil.isNotBlank(url)) {
            ThrowUtils.throwIf(url.length() > 512, ResultCode.PARAMS_ERROR, "url 过长");
        }
        if (StrUtil.isNotBlank(introduction)) {
            ThrowUtils.throwIf(introduction.length() > 256, ResultCode.PARAMS_ERROR, "简介过长");
        }
    }


    @Override
    public PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest) {
        ThrowUtils.throwIf(inputSource == null, ResultCode.PARAMS_ERROR, "图片为空");
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(loginUser == null, ResultCode.NO_AUTH_ERROR);
        // 用于判断是新增还是更新图片  
        Long pictureId = null;
        if (pictureUploadRequest != null) {
            pictureId = pictureUploadRequest.getId();
        }
        // 如果是更新图片，需要校验图片是否存在  
        if (pictureId != null) {
            Picture oldPicture = this.getById(pictureId);
            ThrowUtils.throwIf(oldPicture == null, ResultCode.NOT_FOUND_ERROR, "图片不存在");
            // 仅本人或管理员可编辑
            if (!oldPicture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
                throw new BusinessException(ResultCode.NO_AUTH_ERROR);
            }
        }
        // 上传图片，得到信息  
        // 按照用户 id 划分目录  
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        // 根据 inputSource 类型区分上传方式
        PictureUploadTemplate pictureUploadTemplate = filePictureUpload;
        if (inputSource instanceof String) {
            pictureUploadTemplate = urlPictureUpload;
        }
        UploadPictureResult uploadPictureResult = pictureUploadTemplate.uploadPicture(inputSource, uploadPathPrefix);
        // 构造要入库的图片信息
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());
        // 补充审核参数
        this.fillReviewParams(picture, loginUser);
        // 如果 pictureId 不为空，表示更新，否则是新增
        if (pictureId != null) {
            // 如果是更新，需要补充 id 和编辑时间
            picture.setId(pictureId);
            picture.setEditTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR, "图片上传失败");
        picture = this.getById(picture.getId());
        PictureVO pictureVO = PictureVO.objToVo(picture);
        pictureVO.setUser(userService.getUserVO(loginUser));
        return pictureVO;
    }

    @Override
    public Boolean deletePicture(Long picId) {
        ThrowUtils.throwIf(picId == null || picId <= 0, ResultCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser();
        // 判断是否存在
        Picture oldPicture = this.getById(picId);
        ThrowUtils.throwIf(oldPicture == null, ResultCode.NOT_FOUND_ERROR);
        // 仅管理员或本人可删除
        if (!oldPicture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(picId);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public Boolean updatePicture(PictureUpdateRequest pictureUpdateRequest) {
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureUpdateRequest, picture);
        List<String> tags = pictureUpdateRequest.getTags();
        if (tags != null) {
            picture.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        this.validPicture(picture);
        User loginUser = userService.getLoginUser();
        // 补充审核参数
        this.fillReviewParams(picture, loginUser);
        // 判断是否存在
        Long id = pictureUpdateRequest.getId();
        Picture oldPicture = this.getById(id);
        ThrowUtils.throwIf(oldPicture == null, ResultCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.updateById(picture);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public Boolean editPicture(PictureEditRequest pictureEditRequest) {
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureEditRequest, picture);
        List<String> tags = pictureEditRequest.getTags();
        if (tags != null) {
            picture.setTags(JSONUtil.toJsonStr(tags));
        }
        // 设置编辑时间
        picture.setEditTime(new Date());
        // 数据校验
        this.validPicture(picture);
        // 判断是否存在
        Long id = pictureEditRequest.getId();
        Picture oldPicture = this.getById(id);
        ThrowUtils.throwIf(oldPicture == null, ResultCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        User loginUser = userService.getLoginUser();
        if (!oldPicture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        // 补充审核参数
        this.fillReviewParams(picture, loginUser);
        // 操作数据库
        boolean result = this.updateById(picture);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        Long picSize = pictureQueryRequest.getPicSize();
        Integer picWidth = pictureQueryRequest.getPicWidth();
        Integer picHeight = pictureQueryRequest.getPicHeight();
        Double picScale = pictureQueryRequest.getPicScale();
        String picFormat = pictureQueryRequest.getPicFormat();
        String searchText = pictureQueryRequest.getSearchText();
        Long userId = pictureQueryRequest.getUserId();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 从多字段中搜索
        if (StrUtil.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or().like("introduction", searchText));
        }
        // 精准查询
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(category), "category", category);
        queryWrapper.eq(ObjUtil.isNotEmpty(picWidth), "pic_width", picWidth);
        queryWrapper.eq(ObjUtil.isNotEmpty(picHeight), "pic_height", picHeight);
        queryWrapper.eq(ObjUtil.isNotEmpty(picSize), "pic_size", picSize);
        queryWrapper.eq(ObjUtil.isNotEmpty(picScale), "pic_scale", picScale);
        // 模糊查询
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(introduction), "introduction", introduction);
        queryWrapper.like(StrUtil.isNotBlank(picFormat), "pic_format", picFormat);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public QueryWrapper<Picture> getQueryWrapperAndReview(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = this.getQueryWrapper(pictureQueryRequest);
        Integer reviewStatus = pictureQueryRequest.getReviewStatus();
        String reviewMessage = pictureQueryRequest.getReviewMessage();
        Long reviewerId = pictureQueryRequest.getReviewerId();
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewStatus), "review_status", reviewStatus);
        queryWrapper.like(StrUtil.isNotBlank(reviewMessage), "review_message", reviewMessage);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewerId), "reviewer_id", reviewerId);
        return queryWrapper;
    }

    @Override
    public PictureVO getPictureVO(Picture picture) {
        // 对象转封装类
        PictureVO pictureVO = PictureVO.objToVo(picture);
        // 关联查询用户信息
        Long userId = picture.getUserId();
        if (userId != null && userId > 0) {
            UserVO userVO = userService.getUserVO(userId);
            pictureVO.setUser(userVO);
        }
        return pictureVO;
    }

    @Override
    public Page<PictureVO> getPictureVOPage(Page<Picture> picturePage) {
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureVO> pictureVOPage = new Page<>(picturePage.getCurrent(), picturePage.getSize(), picturePage.getTotal());
        if (CollUtil.isEmpty(pictureList)) {
            return pictureVOPage;
        }
        // 对象列表 -> 封装对象列表
        List<PictureVO> pictureVOList = pictureList.stream().map(PictureVO::objToVo).toList();
        // 关联查询用户信息
        Set<Long> userIdSet = pictureList.stream().map(Picture::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        pictureVOList.forEach(pictureVO -> {
            Long userId = pictureVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            pictureVO.setUser(userService.getUserVO(user));
        });
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }

    @Override
    public void doPictureReview(PictureReviewRequest pictureReviewRequest) {
        Long id = pictureReviewRequest.getId();
        Integer reviewStatus = pictureReviewRequest.getReviewStatus();
        String reviewMessage = pictureReviewRequest.getReviewMessage();
        ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
        ThrowUtils.throwIf(id == null || reviewStatusEnum == null || ReviewStatusEnum.REVIEWING.equals(reviewStatusEnum), ResultCode.PARAMS_ERROR);
        // 判断是否存在
        Picture oldPicture = this.getById(id);
        ThrowUtils.throwIf(oldPicture == null, ResultCode.NOT_FOUND_ERROR);
        // 已经是该状态
        ThrowUtils.throwIf(oldPicture.getReviewStatus().equals(reviewStatus), ResultCode.PARAMS_ERROR, "请勿重复提交审核");
        // 更新审核状态
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
        User loginUser = userService.getLoginUser();
        Picture updatePicture = new Picture();
        BeanUtils.copyProperties(pictureReviewRequest, updatePicture);
        updatePicture.setReviewerId(loginUser.getId());
        updatePicture.setReviewMessage(reviewMessage);
        updatePicture.setReviewTime(new Date());
        boolean result = this.updateById(updatePicture);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
    }

    @Override
    public void fillReviewParams(Picture picture, User loginUser) {
        if (userService.isAdmin(loginUser)) {
            // 管理员自动过审
            picture.setReviewStatus(ReviewStatusEnum.PASS.getValue());
            picture.setReviewerId(loginUser.getId());
            picture.setReviewMessage("管理员自动过审");
            picture.setReviewTime(new Date());
        } else {
            // 非管理员，创建或编辑都要改为待审核
            picture.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        }
    }

}



