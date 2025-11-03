package com.lilemy.xiaoxinshu.model.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author lilemy
 * @date 2025-10-04 23:38
 */
@Getter
public enum SysUserRoleEnum {

    USER("普通用户", 0),
    ADMIN("管理员", 1),
    BAN("封禁用户", -1);

    private final String text;

    private final Integer value;

    SysUserRoleEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }
}
