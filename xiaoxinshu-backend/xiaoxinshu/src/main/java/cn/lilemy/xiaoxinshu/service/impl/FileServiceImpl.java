package cn.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadFileRequest;
import cn.lilemy.xiaoxinshu.model.enums.FileUploadBizEnum;
import cn.lilemy.xiaoxinshu.service.FileService;
import cn.lilemy.xiaoxinshu.strategy.FileStrategy;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 文件上传服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    @Override
    public void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String suffix = FileUtil.getPrefix(multipartFile.getOriginalFilename());
        final long ONE_M = 10 * 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "文件大小不能超过 10M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(suffix)) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }

    @Override
    public String uploadFile(MultipartFile file, UploadFileRequest uploadFileRequest) {
        // 判断文件是否为空
        ThrowUtils.throwIf(file == null || file.getSize() == 0, ResultCode.PARAMS_ERROR, "文件不能为空");
        // 文件的原始名称
        String originalFileName = file.getOriginalFilename();
        ThrowUtils.throwIf(originalFileName == null, ResultCode.PARAMS_ERROR, "文件名字不能为空");
        // 获取业务信息
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(fileUploadBizEnum == null || file.getSize() == 0, ResultCode.PARAMS_ERROR, "未知的业务类型");
        Long bizId = uploadFileRequest.getBizId();
        ThrowUtils.throwIf(bizId == null || bizId <= 0, ResultCode.PARAMS_ERROR, "业务 id 不存在");
        validFile(file, fileUploadBizEnum);
        // 生成存储对象的名称
        String uuid = RandomStringUtils.randomAlphanumeric(32);
        // 获取文件的后缀，如 .jpg
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 拼接上文件后缀，即为要存储的文件名
        String objectName = String.format("%s%s", uuid, suffix);
        String filePath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), bizId, objectName);
        return fileStrategy.uploadFile(file, filePath);
    }
}
