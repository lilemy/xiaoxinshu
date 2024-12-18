package cn.lilemy.xiaoxinshu.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片编辑请求体
 */
@Data
public class PictureEditRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1938992332150948350L;

    /**
     * id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

}
