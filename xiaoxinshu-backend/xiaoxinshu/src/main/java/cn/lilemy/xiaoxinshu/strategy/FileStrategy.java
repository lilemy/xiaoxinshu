package cn.lilemy.xiaoxinshu.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件策略接口
 */
public interface FileStrategy {

    /**
     * 文件上传
     *
     * @param file     文件
     * @param filePath 文件路径
     * @return 文件 url
     */
    String uploadFile(MultipartFile file, String filePath);
}
