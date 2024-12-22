package cn.lilemy.xiaoxinshu.manager.upload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.lilemy.xiaoxinshu.config.CosClientConfig;
import cn.lilemy.xiaoxinshu.manager.CosManager;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadPictureResult;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;

@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    protected CosManager cosManager;

    @Resource
    protected CosClientConfig cosClientConfig;

    /**
     * 模版方法，定义上传流程
     *
     * @param inputSource      输入源
     * @param uploadPathPrefix 上传路径前缀
     * @return 图片信息封装返回
     */
    public final UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1.校验图片
        validPicture(inputSource);
        // 2.图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originFilename = getOriginalFilename(inputSource);
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 3.创建临时文件
            file = File.createTempFile(uploadPath, null);
            // 处理文件来源（本地或 URL）
            processFile(inputSource, file);
            // 4.上传图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 5.封装返回信息
            return buildResult(originFilename, file, uploadPath, imageInfo);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 6.清理临时文件
            deleteTempFile(file);
        }
    }

    /**
     * 图片校验
     *
     * @param inputSource 输入源
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取输入源的原始文件名
     *
     * @param inputSource 输入源
     * @return 原始文件名
     */
    protected abstract String getOriginalFilename(Object inputSource);

    /**
     * 处理输入源并生成本地临时文件
     *
     * @param inputSource 输入源
     * @param file        文件
     * @throws Exception 异常处理
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 封装返回结果
     *
     * @param originFilename 源图片信息
     * @param file           图片
     * @param uploadPath     上传路径
     * @param imageInfo      图片信息
     * @return 图片信息封装结果
     */
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(imageInfo.getFormat());
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        return uploadPictureResult;
    }

    /**
     * 删除临时文件
     *
     * @param file 临时文件
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        // 删除临时文件
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }
}
