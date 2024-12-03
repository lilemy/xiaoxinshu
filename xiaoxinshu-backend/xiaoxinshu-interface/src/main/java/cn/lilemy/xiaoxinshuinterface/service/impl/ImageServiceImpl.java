package cn.lilemy.xiaoxinshuinterface.service.impl;

import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshuinterface.mapper.ImageMapper;
import cn.lilemy.xiaoxinshuinterface.model.entity.Image;
import cn.lilemy.xiaoxinshuinterface.service.ImageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author qq233
 * @description 针对表【image(图片)】的数据库操作Service实现
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
        implements ImageService {

    @Resource
    private ImageMapper imageMapper;

    @Override
    public String getRandomImage(String type) {
        if (StringUtils.isNotBlank(type)) {
            // 查询总记录数
            QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type);
            long count = this.count(queryWrapper);
            ThrowUtils.throwIf(count == 0, ResultCode.NOT_FOUND_ERROR, "No images found");
            // 生成随机偏移量
            long offset = new Random().nextLong(count);
            // 查询随机图片
            return imageMapper.getRandomImageByType(type, offset);
        } else {
            // 查询总记录数
            long count = this.count();
            ThrowUtils.throwIf(count == 0, ResultCode.NOT_FOUND_ERROR, "No images found");
            // 生成随机偏移量
            long offset = new Random().nextLong(count);
            // 查询随机图片
            return imageMapper.getRandomImage(offset);
        }
    }
}




