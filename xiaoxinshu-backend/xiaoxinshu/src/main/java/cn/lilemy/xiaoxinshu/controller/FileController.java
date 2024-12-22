package cn.lilemy.xiaoxinshu.controller;

import cn.lilemy.xiaoxinshu.manager.upload.FilePictureUpload;
import cn.lilemy.xiaoxinshu.manager.upload.PictureUploadTemplate;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadFileRequest;
import cn.lilemy.xiaoxinshu.model.dto.file.UploadPictureResult;
import cn.lilemy.xiaoxinshu.model.enums.FileUploadBizEnum;
import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
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
    private FilePictureUpload filePictureUpload;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file, UploadFileRequest uploadFileRequest) {
        // 获取业务信息
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(fileUploadBizEnum == null, ResultCode.PARAMS_ERROR, "未知的业务类型");
        Long bizId = uploadFileRequest.getBizId();
        ThrowUtils.throwIf(bizId == null || bizId <= 0, ResultCode.PARAMS_ERROR, "业务 id 不存在");
        // 存储的文件名
        String filePath = String.format("%s/%s", fileUploadBizEnum.getValue(), bizId);
        PictureUploadTemplate pictureUploadTemplate = filePictureUpload;
        UploadPictureResult uploadPictureResult = pictureUploadTemplate.uploadPicture(file, filePath);
        String url = uploadPictureResult.getUrl();
        return ResultUtils.success(url);
    }
}
