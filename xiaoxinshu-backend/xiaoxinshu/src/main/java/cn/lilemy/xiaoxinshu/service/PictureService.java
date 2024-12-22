package cn.lilemy.xiaoxinshu.service;

import cn.lilemy.xiaoxinshu.model.dto.picture.*;
import cn.lilemy.xiaoxinshu.model.vo.PictureVO;
import cn.lilemy.xiaoxinshucommon.model.entity.Picture;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author qq233
 * @description 针对表【picture(图片)】的数据库操作Service
 */
public interface PictureService extends IService<Picture> {

    /**
     * 校验数据
     *
     * @param picture 图片信息
     */
    void validPicture(Picture picture);

    /**
     * 上传图片
     *
     * @param inputSource          上传的图片
     * @param pictureUploadRequest 图片上传请求
     * @return 图片封装信息
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest);

    /**
     * 删除图片
     *
     * @param picId 图片 id
     * @return 是否删除成功
     */
    Boolean deletePicture(Long picId);

    /**
     * 更新图片
     *
     * @param pictureUpdateRequest 图片更新请求体
     * @return 是否更新成功
     */
    Boolean updatePicture(PictureUpdateRequest pictureUpdateRequest);

    /**
     * 编辑图片
     *
     * @param pictureEditRequest 图片编辑请求体
     * @return 是否编辑成功
     */
    Boolean editPicture(PictureEditRequest pictureEditRequest);

    /**
     * 图片分页查询
     *
     * @param pictureQueryRequest 分页查询请求体
     * @return 分页查询请求
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 图片分页查询（包含审核信息）
     *
     * @param pictureQueryRequest 分页查询请求体
     * @return 分页查询请求
     */
    QueryWrapper<Picture> getQueryWrapperAndReview(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取单个图片封装
     *
     * @param picture 图片信息
     * @return 图片封装信息
     */
    PictureVO getPictureVO(Picture picture);

    /**
     * 获取图片分页封装
     *
     * @param picturePage 图片分页信息
     * @return 图片分页封装信息
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest 管理员审核图片请求
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest);

    void fillReviewParams(Picture picture, User loginUser);
}