package cn.lilemy.xiaoxinshu.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片更新请求体
 */
@Data
public class PictureUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5429937147255200603L;

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