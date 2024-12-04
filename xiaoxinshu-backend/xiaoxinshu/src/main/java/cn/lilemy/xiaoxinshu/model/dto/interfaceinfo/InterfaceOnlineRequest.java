package cn.lilemy.xiaoxinshu.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class InterfaceOnlineRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -3956516267356003778L;

    /**
     * id
     */
    private Long id;
}
