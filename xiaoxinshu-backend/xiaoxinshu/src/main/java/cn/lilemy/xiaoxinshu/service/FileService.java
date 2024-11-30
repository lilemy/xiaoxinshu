package cn.lilemy.xiaoxinshu.service;

import cn.lilemy.xiaoxinshu.model.dto.file.UploadFileRequest;
import cn.lilemy.xiaoxinshu.model.enums.FileUploadBizEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 */
public interface FileService {

    /**
     * 文件
     *
     * @param multipartFile     文件
     * @param fileUploadBizEnum 业务类型
     */
    void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum);

    /**
     * 文件上传
     *
     * @param file              文件
     * @param uploadFileRequest 文件上传请求体
     * @return 文件对应 url
     */
    String uploadFile(MultipartFile file, UploadFileRequest uploadFileRequest);
}
