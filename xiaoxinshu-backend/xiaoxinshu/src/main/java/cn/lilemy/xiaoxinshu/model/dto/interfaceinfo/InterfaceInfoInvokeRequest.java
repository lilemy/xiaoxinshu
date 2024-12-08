package cn.lilemy.xiaoxinshu.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口请求参数
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3000310081663252683L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;
}