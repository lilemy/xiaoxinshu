package com.lilemy.xiaoxinshu;

import com.lilemy.xiaoxinshu.manager.rustfs.OssHelper;
import com.lilemy.xiaoxinshu.manager.rustfs.entity.OssVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.List;

/**
 * 对象存储测试
 *
 * @author lilemy
 * @date 2026-01-08 14:37
 */
@Slf4j
@SpringBootTest
public class OssTest {

    @Resource
    private OssHelper ossHelper;

    @Test
    void testOssUpload() {
        String testFile = "hello.txt";
        // 上传 (确保项目根目录下有 hello.txt)
        String test = ossHelper.uploadFile("test", Paths.get(testFile));
        System.out.println(test);
    }

    @Test
    void testOssList() {
        List<OssVo> fileList = ossHelper.getFileList(null);
        fileList.forEach(file -> log.info("文件: {}", file));
    }

    @Test
    void testOssDownload() {
        // 下载
        ossHelper.downloadFile("test/2026/01/08/c054dcf5fe5249099241d94741efb16f.txt", Paths.get("downloaded-from-s3.txt"));
    }

    @Test
    void testOssDownloadByUrl() {
        // 下载
        ossHelper.downloadFileByUrl("https://pic.lilemy.cn/xiaoxinshu-dev/test/2026/01/08/9e2e8ac7d4de4c32a98f214ce37dc0ae.jpg", Paths.get("downloaded-from-s3.jpg"));
    }

    @Test
    void testOssDelete() {
        // 删除
        ossHelper.deleteFileByUrl("https://pic.lilemy.cn/xiaoxinshu-dev/test/2026/01/08/9e2e8ac7d4de4c32a98f214ce37dc0ae.jpg");
    }
}
