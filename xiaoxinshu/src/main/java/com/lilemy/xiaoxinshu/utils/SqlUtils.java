package com.lilemy.xiaoxinshu.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * SQL 工具
 *
 * @author lilemy
 * @date 2025/06/03 18:30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 排序字段
     * @return {@link Boolean}
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }

}
