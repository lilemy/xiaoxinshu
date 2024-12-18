package cn.lilemy.xiaoxinshuinterface.controller;

import cn.lilemy.xiaoxinshucommon.common.BaseResponse;
import cn.lilemy.xiaoxinshucommon.common.ResultUtils;
import cn.lilemy.xiaoxinshuinterface.service.ImageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    private ImageService imageService;

    @GetMapping("/random")
    public BaseResponse<String> getRandomImage(@RequestParam(required = false) String type) {
        String imageUrl = imageService.getRandomImage(type);
        return ResultUtils.success(imageUrl);
    }
}
