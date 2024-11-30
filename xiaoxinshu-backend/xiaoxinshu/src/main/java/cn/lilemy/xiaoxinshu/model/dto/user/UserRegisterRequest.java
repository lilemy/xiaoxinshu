package cn.lilemy.xiaoxinshu.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8448473981019189941L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
