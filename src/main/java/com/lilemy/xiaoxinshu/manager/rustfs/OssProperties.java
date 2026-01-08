package com.lilemy.xiaoxinshu.manager.rustfs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RustFS 配置
 *
 * @author lilemy
 * @date 2026-01-08 14:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.s3")
public class OssProperties {

    /**
     * RustFS 地址
     */
    private String endpoint;

    /**
     * RustFS 区域
     */
    private String region;

    /**
     * RustFS 访问密钥
     */
    private String accessKey;

    /**
     * RustFS 密钥
     */
    private String secretKey;

    /**
     * RustFS 存储桶名称
     */
    private String bucketName;
}
