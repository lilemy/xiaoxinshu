package cn.lilemy.xiaoxinshu.model.dto.space;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户创建空间请求
 */
@Data
public class SpaceCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7653655692302362055L;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 空间级别：0-普通版 1-专业版 2-旗舰版
     */
    private Integer spaceLevel;

}
