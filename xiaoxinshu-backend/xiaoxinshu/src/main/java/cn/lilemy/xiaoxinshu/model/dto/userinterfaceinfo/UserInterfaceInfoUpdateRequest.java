package cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户接口关系更新请求体
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1098194937406741031L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;
}
