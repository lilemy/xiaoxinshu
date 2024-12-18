package cn.lilemy.xiaoxinshu.controller;

import cn.lilemy.xiaoxinshu.manager.FileManager;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadFileRequest;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name = "FileController")
public class FileController {

    @Resource
    private FileManager fileManager;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file, UploadFileRequest uploadFileRequest) {
        String url = fileManager.uploadFile(file, uploadFileRequest);
        return ResultUtils.success(url);
    }
}
