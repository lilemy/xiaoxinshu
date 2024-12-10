package cn.lilemy.xiaoxinshu.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class InterfaceInfoByName implements Serializable {

    @Serial
    private static final long serialVersionUID = 7644859636261715536L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
}
