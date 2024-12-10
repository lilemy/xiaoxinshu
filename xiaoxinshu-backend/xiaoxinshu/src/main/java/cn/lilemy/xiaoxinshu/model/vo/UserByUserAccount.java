package cn.lilemy.xiaoxinshu.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户 id，名称
 */
@Data
public class UserByUserAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = 8964358540441989659L;

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

}
