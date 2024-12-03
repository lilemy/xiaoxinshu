package cn.lilemy.xiaoxinshuinterface.mapper;

import cn.lilemy.xiaoxinshuinterface.model.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author qq233
 * @description 针对表【image(图片)】的数据库操作Mapper
 * @Entity cn.lilemy.xiaoxinshuinterface.model.entity.Image
 */
public interface ImageMapper extends BaseMapper<Image> {

    // 查询随机偏移量的图片 url
    @Select("SELECT image.url FROM image LIMIT #{offset}, 1")
    String getRandomImage(long offset);

    @Select("SELECT image.url FROM image WHERE type=#{type} LIMIT #{offset}, 1")
    String getRandomImageByType(String type, long offset);
}




