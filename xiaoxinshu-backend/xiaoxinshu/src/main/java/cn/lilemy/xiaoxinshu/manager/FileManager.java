package cn.lilemy.xiaoxinshu.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.lilemy.xiaoxinshu.config.CosClientConfig;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadFileRequest;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadPictureResult;
import cn.lilemy.xiaoxinshu.model.enums.FileUploadBizEnum;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FileManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosManager cosManager;

    public String uploadFile(MultipartFile multipartFile, UploadFileRequest uploadFileRequest) {
        // 判断文件是否为空
        ThrowUtils.throwIf(multipartFile == null || multipartFile.getSize() == 0, ResultCode.PARAMS_ERROR, "文件不能为空");
        // 文件的原始名称
        String originalFileName = multipartFile.getOriginalFilename();
        ThrowUtils.throwIf(originalFileName == null, ResultCode.PARAMS_ERROR, "文件名字不能为空");
        // 获取业务信息
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(fileUploadBizEnum == null || multipartFile.getSize() == 0, ResultCode.PARAMS_ERROR, "未知的业务类型");
        Long bizId = uploadFileRequest.getBizId();
        ThrowUtils.throwIf(bizId == null || bizId <= 0, ResultCode.PARAMS_ERROR, "业务 id 不存在");
        validPicture(multipartFile);
        // 生成存储对象的名称
        String uuid = RandomStringUtils.randomAlphanumeric(32);
        // 获取文件的后缀，如 .jpg
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 拼接上文件后缀，即为要存储的文件名
        String objectName = String.format("%s%s", uuid, suffix);
        String filePath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), bizId, objectName);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            // 上传文件
            cosManager.putObject(filePath, file);
            // 返回可访问地址
            return cosClientConfig.getHost() + filePath;
        } catch (Exception e) {
            log.error("图片上传到对象存储失败，filePath：{}", filePath, e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "上传失败");
        } finally {
            this.deleteTempFile(file);
        }
    }

    /**
     * 上传图片
     *
     * @param multipartFile    文件
     * @param uploadPathPrefix 上传路径前缀
     * @return {@link UploadPictureResult}
     */
    public UploadPictureResult uploadPicture(MultipartFile multipartFile, String uploadPathPrefix) {
        // 校验图片  
        validPicture(multipartFile);
        // 图片上传地址  
        String uuid = RandomUtil.randomString(16);
        String originFilename = multipartFile.getOriginalFilename();
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 创建临时文件  
            file = File.createTempFile(uploadPath, null);
            multipartFile.transferTo(file);
            // 上传图片  
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 封装返回结果  
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
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "上传失败");
        } finally {
            this.deleteTempFile(file);
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile multipart 文件
     */
    public void validPicture(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ResultCode.PARAMS_ERROR, "文件不能为空");
        // 1. 校验文件大小  
        long fileSize = multipartFile.getSize();
        final long ONE_M = 1024 * 1024L;
        ThrowUtils.throwIf(fileSize > 5 * ONE_M, ResultCode.PARAMS_ERROR, "文件大小不能超过 5M");
        // 2. 校验文件后缀  
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // 允许上传的文件后缀  
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "jpg", "png", "webp");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix), ResultCode.PARAMS_ERROR, "文件类型错误");
    }

    /**
     * 删除临时文件
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
