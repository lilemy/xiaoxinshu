package cn.lilemy.xiaoxinshu.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class InterfaceOfflineRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4162082142677568679L;

    /**
     * id
     */
    private Long id;
}
