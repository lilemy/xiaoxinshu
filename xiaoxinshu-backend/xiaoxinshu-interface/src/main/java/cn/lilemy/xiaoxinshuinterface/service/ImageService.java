package cn.lilemy.xiaoxinshuinterface.service;

import cn.lilemy.xiaoxinshuinterface.model.entity.Image;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author qq233
 * @description 针对表【image(图片)】的数据库操作Service
 */
public interface ImageService extends IService<Image> {

    /**
     * 根据类型随机获取图片
     *
     * @param type 图片类型（为空时，从全部图片中随机）
     * @return 图片 url
     */
    String getRandomImage(String type);
}
