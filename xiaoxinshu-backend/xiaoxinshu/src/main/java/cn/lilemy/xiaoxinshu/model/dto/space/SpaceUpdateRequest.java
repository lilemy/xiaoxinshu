package cn.lilemy.xiaoxinshu.model.dto.space;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 空间更新请求（仅管理员可用）
 */
@Data
public class SpaceUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8420354393432121587L;

    /**
     * id
     */
    private Long id;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 空间级别：0-普通版 1-专业版 2-旗舰版
     */
    private Integer spaceLevel;

    /**
     * 空间图片的最大总大小
     */
    private Long maxSize;

    /**
     * 空间图片的最大数量
     */
    private Long maxCount;

}
