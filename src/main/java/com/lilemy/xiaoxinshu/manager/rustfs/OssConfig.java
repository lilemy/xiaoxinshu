package com.lilemy.xiaoxinshu.manager.rustfs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * RustFS 配置
 *
 * @author lilemy
 * @date 2026-01-08 14:58
 */
@Configuration
public class OssConfig {

    /**
     * 创建 S3 客户端
     *
     * @param properties RustFS 配置
     * @return S3 客户端
     */
    @Bean
    public S3Client s3Client(OssProperties properties) {
        return S3Client.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())
                        )
                )
                .forcePathStyle(true) // RustFS 必须开启
                .build();
    }
}
