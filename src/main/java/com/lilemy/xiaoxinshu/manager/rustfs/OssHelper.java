package com.lilemy.xiaoxinshu.manager.rustfs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lilemy.xiaoxinshu.common.ResultCode;
import com.lilemy.xiaoxinshu.exception.BusinessException;
import com.lilemy.xiaoxinshu.exception.ThrowUtils;
import com.lilemy.xiaoxinshu.manager.rustfs.entity.OssVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 对象存储帮助类
 *
 * @author lilemy
 * @date 2026-01-08 15:01
 */
@Slf4j
@Component
public class OssHelper {

    @Resource
    private S3Client s3Client;

    @Resource
    private OssProperties ossProperties;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 上传文件 (基于 Path)
     *
     * @param prefix   前缀
     * @param filePath 文件路径
     * @return 文件上传后的完整路径
     */
    public String uploadFile(String prefix, Path filePath) {
        String originalFilename = filePath.getFileName().toString();
        String suffix = FileUtil.getSuffix(originalFilename);
        ThrowUtils.throwIf(StringUtils.isEmpty(suffix), ResultCode.PARAMS_ERROR, "请上传正确的文件格式");
        String objectKey = getPath(prefix, suffix);
        log.info("文件上传开始: {}", objectKey);
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .build(),
                filePath
        );
        log.info("文件上传成功: {}", objectKey);
        return getFullPath(objectKey);
    }

    /**
     * 上传文件 (基于MultipartFile)
     *
     * @param prefix        前缀
     * @param multipartFile 文件
     * @return 文件上传后的完整路径
     */
    public String uploadFile(String prefix, MultipartFile multipartFile) {
        // 自动提取后缀名
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        ThrowUtils.throwIf(StringUtils.isEmpty(suffix), ResultCode.PARAMS_ERROR, "请上传正确的文件格式");
        String objectKey = getPath(prefix, suffix);
        try (InputStream is = multipartFile.getInputStream()) {
            return uploadInputStream(objectKey, is, multipartFile.getSize());
        } catch (IOException e) {
            log.error("文件流读取失败", e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "文件上传失败");
        }
    }

    /**
     * 上传文件 (基于通用输入流)
     *
     * @param prefix        前缀
     * @param suffix        后缀
     * @param inputStream   输入流
     * @param contentLength 流的大小
     * @return 文件上传后的完整路径
     */
    public String uploadFile(String prefix, String suffix, InputStream inputStream, long contentLength) {
        String objectKey = getPath(prefix, suffix);
        return uploadInputStream(objectKey, inputStream, contentLength);
    }

    /**
     * 列出文件列表
     *
     * @param prefix 前缀，文件夹以 / 结尾
     * @return 文件列表
     */
    public List<OssVo> getFileList(String prefix) {
        // 列出对象
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(
                ListObjectsV2Request.builder()
                        .bucket(ossProperties.getBucketName())
                        .prefix(prefix)
                        .build());
        return listResponse.contents().stream().map(object -> {
            OssVo vo = new OssVo();
            vo.setUrl(getFullPath(object.key()));
            vo.setSize(object.size());
            return vo;
        }).toList();
    }

    /**
     * 根据完整路径下载文件
     *
     * @param fullPath        文件路径
     * @param destinationPath 文件保存路径
     */
    public void downloadFileByUrl(String fullPath, Path destinationPath) {
        String objectKey = extractObjectKey(fullPath);
        downloadFile(objectKey, destinationPath);
    }

    /**
     * 下载文件
     *
     * @param objectKey       文件路径
     * @param destinationPath 文件保存路径
     */
    public void downloadFile(String objectKey, Path destinationPath) {
        log.info("文件下载开始: {}", objectKey);
        s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .build(),
                destinationPath
        );
        log.info("文件下载成功: {}", objectKey);
    }

    /**
     * 根据完整路径删除文件
     *
     * @param fullPath 完整路径
     */
    public void deleteFileByUrl(String fullPath) {
        String objectKey = extractObjectKey(fullPath);
        deleteFile(objectKey);
    }

    /**
     * 删除文件
     *
     * @param objectKey 文件路径
     */
    public void deleteFile(String objectKey) {
        log.info("文件删除开始: {}", objectKey);
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .build()
        );
        log.info("文件删除成功: {}", objectKey);
    }

    /**
     * 生成一个符合特定规则的、唯一的文件路径。通过使用日期、UUID、前缀和后缀等元素的组合
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 文件路径
     */
    public String getPath(String prefix, String suffix) {
        // 生成 uuid
        String uuid = IdUtil.fastSimpleUUID();
        // 生成日期路径
        String datePath = LocalDate.now().format(DATE_FORMATTER);
        // 拼接路径
        String path = StringUtils.isNotEmpty(prefix) ?
                prefix + "/" + datePath + "/" + uuid : datePath + "/" + uuid;
        // 判断后缀是否有 . 如果没有，则添加
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        return path + suffix;
    }

    /**
     * 获取文件的完整路径
     *
     * @param objectKey 文件路径
     * @return 文件完整路径
     */
    public String getFullPath(String objectKey) {
        return ossProperties.getEndpoint() + "/" + ossProperties.getBucketName() + "/" + objectKey;
    }

    /**
     * 获取文件对象存储中的路径
     * 全路径 = Endpoint + "/" + Bucket + "/" + 对象存储中的路径
     *
     * @param fullPath 文件的完整路径
     * @return 文件对象存储中的路径
     */
    private String extractObjectKey(String fullPath) {
        ThrowUtils.throwIf(StringUtils.isBlank(fullPath), ResultCode.PARAMS_ERROR, "文件路径不能为空");
        // 构造前缀
        String endpoint = ossProperties.getEndpoint();
        String bucket = ossProperties.getBucketName();
        // 确保 endpoint 不以 / 结尾，方便拼接
        String prefix = StrUtil.removeSuffix(endpoint, "/") + "/" + bucket + "/";
        if (fullPath.startsWith(prefix)) {
            // 截取前缀之后的部分
            return fullPath.substring(prefix.length());
        } else {
            // 如果 URL 格式不符合预期，尝试处理特殊情况或者抛出异常
            log.warn("全路径格式不符合配置前缀: {}, 尝试解析...", fullPath);
            // 兜底
            return fullPath;
        }
    }

    /**
     * 通过 RequestBody 处理流上传
     *
     * @param objectKey     文件路径
     * @param inputStream   输入流
     * @param contentLength 文件长度
     * @return 文件上传后的完整路径
     */
    private String uploadInputStream(String objectKey, InputStream inputStream, long contentLength) {
        log.info("文件流上传开始: {}", objectKey);
        String contentType = FileUtil.getMimeType(objectKey);
        ThrowUtils.throwIf(StringUtils.isEmpty(contentType), ResultCode.PARAMS_ERROR, "请上传正确的文件格式");
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, contentLength)
        );
        log.info("文件流上传成功: {}", objectKey);
        return getFullPath(objectKey);
    }
}
