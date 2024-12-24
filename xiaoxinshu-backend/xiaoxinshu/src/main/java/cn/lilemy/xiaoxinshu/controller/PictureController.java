package cn.lilemy.xiaoxinshu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.lilemy.xiaoxinshu.constant.UserConstant;
import cn.lilemy.xiaoxinshu.model.dto.picture.*;
import cn.lilemy.xiaoxinshu.model.enums.ReviewStatusEnum;
import cn.lilemy.xiaoxinshu.model.vo.PictureVO;
import cn.lilemy.xiaoxinshu.service.PictureService;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.DeleteRequest;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.Picture;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 图片接口
 */
@Slf4j
@RestController
@RequestMapping("/pic")
@Tag(name = "PictureController")
public class PictureController {

    @Resource
    private PictureService pictureService;

    // region 增删改查

    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public BaseResponse<PictureVO> uploadPicture(
            @RequestParam("file") MultipartFile multipartFile,
            PictureUploadRequest pictureUploadRequest) {
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest);
        return ResultUtils.success(pictureVO);
    }

    @Operation(summary = "上传图片根据 URL")
    @PostMapping("/upload/url")
    public BaseResponse<PictureVO> uploadPictureByUrl(@RequestBody PictureUploadRequest pictureUploadRequest) {
        String fileUrl = pictureUploadRequest.getFileUrl();
        PictureVO pictureVO = pictureService.uploadPicture(fileUrl, pictureUploadRequest);
        return ResultUtils.success(pictureVO);
    }

    @Operation(summary = "批量上传图片（仅管理员）")
    @PostMapping("/upload/batch")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Integer> uploadPictureByBatch(@RequestBody PictureUploadByBatchRequest pictureUploadByBatchRequest) {
        ThrowUtils.throwIf(pictureUploadByBatchRequest == null, ResultCode.PARAMS_ERROR);
        Integer uploadCount = pictureService.uploadPictureByBatch(pictureUploadByBatchRequest);
        return ResultUtils.success(uploadCount);
    }

    @Operation(summary = "删除图片")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePicture(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        Long id = deleteRequest.getId();
        Boolean result = pictureService.deletePicture(id);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新图片（仅管理员）")
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePicture(@RequestBody PictureUpdateRequest pictureUpdateRequest) {
        ThrowUtils.throwIf(pictureUpdateRequest == null || pictureUpdateRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        Boolean result = pictureService.updatePicture(pictureUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "编辑图片")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editPicture(@RequestBody PictureEditRequest pictureEditRequest) {
        ThrowUtils.throwIf(pictureEditRequest == null || pictureEditRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        Boolean result = pictureService.editPicture(pictureEditRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据 id 获取图片（仅管理员）")
    @GetMapping("/get")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Picture> getPictureById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null, ResultCode.NOT_FOUND_ERROR);
        return ResultUtils.success(picture);
    }

    @Operation(summary = "根据 id 获取图片封装信息")
    @GetMapping("/get/vo")
    public BaseResponse<PictureVO> getPictureVOById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null, ResultCode.NOT_FOUND_ERROR);
        // 未通过审核图片不允许用户查看
        ThrowUtils.throwIf(!picture.getReviewStatus().equals(ReviewStatusEnum.PASS.getValue()), ResultCode.NO_AUTH_ERROR);
        // 获取封装类
        return ResultUtils.success(pictureService.getPictureVO(picture));
    }

    @Operation(summary = "分页获取图片列表（仅管理员）")
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest) {
        int current = pictureQueryRequest.getCurrent();
        int pageSize = pictureQueryRequest.getPageSize();
        // 查询数据库
        Page<Picture> picturePage = pictureService.page(new Page<>(current, pageSize),
                pictureService.getQueryWrapperAndReview(pictureQueryRequest));
        return ResultUtils.success(picturePage);
    }

    @Operation(summary = "分页获取图片封装列表")
    @PostMapping("/list/vo")
    public BaseResponse<Page<PictureVO>> listPictureVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest) {
        // 查询数据库
        Page<PictureVO> pictureVOPage = pictureService.listPictureVOByPageByCache(pictureQueryRequest);
        // 获取封装类
        return ResultUtils.success(pictureVOPage);
    }

    // endregion

    @Operation(summary = "获取图片分类标签列表")
    @GetMapping("/tag_category")
    public BaseResponse<PictureTagCategory> listPictureTagCategory() {
        PictureTagCategory pictureTagCategory = new PictureTagCategory();
        List<String> tagList = Arrays.asList("原神", "崩坏·星穹铁道", "绝区零", "无限暖暖", "我的世界", "高清", "艺术", "校园", "背景", "简历", "创意");
        List<String> categoryList = Arrays.asList("壁纸", "二次元", "表情包", "素材", "明星", "其他");
        pictureTagCategory.setTagList(tagList);
        pictureTagCategory.setCategoryList(categoryList);
        return ResultUtils.success(pictureTagCategory);
    }

    @Operation(summary = "审核图片")
    @PostMapping("/review")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> doPictureReview(@RequestBody PictureReviewRequest pictureReviewRequest) {
        ThrowUtils.throwIf(pictureReviewRequest == null, ResultCode.PARAMS_ERROR);
        pictureService.doPictureReview(pictureReviewRequest);
        return ResultUtils.success(true);
    }

}
