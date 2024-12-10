package cn.lilemy.xiaoxinshu.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserByAccessKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 6981498291336639131L;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

}
