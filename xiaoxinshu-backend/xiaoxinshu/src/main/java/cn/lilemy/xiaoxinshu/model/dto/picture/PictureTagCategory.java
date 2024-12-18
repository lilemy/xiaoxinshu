package cn.lilemy.xiaoxinshu.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片标签分类列表
 */
@Data
public class PictureTagCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = -4108232140389163845L;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 分类列表
     */
    private List<String> categoryList;
}
