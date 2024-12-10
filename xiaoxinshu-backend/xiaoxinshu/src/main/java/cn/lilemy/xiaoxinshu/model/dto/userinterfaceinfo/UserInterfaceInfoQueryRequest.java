package cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo;

import cn.lilemy.xiaoxinshucommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户关系分页查询请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6952855121934881322L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

}
