package cn.lilemy.xiaoxinshuinterface.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图片类型枚举
 */
@Getter
public enum ImageTypeEnum {
    CARTOON("卡通", 10002);
    private final String text;

    private final int value;

    ImageTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 用户权限枚举值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 用户权限枚举值
     * @return 用户权限枚举
     */
    public static ImageTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ImageTypeEnum anEnum : ImageTypeEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
