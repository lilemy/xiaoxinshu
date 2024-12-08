package cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户接口关系添加请求体
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2368722172973993151L;

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;
}
