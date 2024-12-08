package cn.lilemy.xiaoxinshu.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 开放接口枚举
 */
@Getter
public enum InterfaceEnum {

    IMAGE_RANDOM("随机图片接口", "/api/interface/image/random"),
    ;

    private final String text;

    private final String value;

    InterfaceEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 用户权限枚举值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 用户权限枚举值
     * @return 用户权限枚举
     */
    public static InterfaceEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceEnum anEnum : InterfaceEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}

