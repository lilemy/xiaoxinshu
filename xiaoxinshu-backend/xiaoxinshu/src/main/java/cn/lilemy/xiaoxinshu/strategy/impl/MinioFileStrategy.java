package cn.lilemy.xiaoxinshu.strategy.impl;

import cn.lilemy.xiaoxinshu.common.ResultCode;
import cn.lilemy.xiaoxinshu.config.MinioClientConfig;
import cn.lilemy.xiaoxinshu.exception.BusinessException;
import cn.lilemy.xiaoxinshu.strategy.FileStrategy;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * Minio本地存储
 */
@Slf4j
public class MinioFileStrategy implements FileStrategy {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioClientConfig minioClientConfig;

    @Override
    public String uploadFile(MultipartFile file, String filePath) {
        // 文件的 Content-Type
        String contentType = file.getContentType();
        try {
            // 上传文件至 Minio
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioClientConfig.getBucketName())
                    .object(filePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            log.error("cos file upload error, filepath = {}", filePath, e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "上传失败");
        }
        // 返回文件的访问链接
        return String.format("%s/%s%s", minioClientConfig.getEndpoint(), minioClientConfig.getBucketName(), filePath);
    }
}
