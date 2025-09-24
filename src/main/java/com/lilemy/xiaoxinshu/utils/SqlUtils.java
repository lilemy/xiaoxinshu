package com.lilemy.xiaoxinshu.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * SQL 工具
 *
 * @author lilemy
 * @date 2025-08-13 18:53
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtils {

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static final String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 排序字段
     * @return {@link Boolean}
     */
    public static String validSortField(String sortField) {
        if (StringUtils.isNotEmpty(sortField) && !isValidOrderBySql(sortField)) {
            throw new IllegalArgumentException("参数不符合规范，不能进行查询");
        }
        return sortField;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }
}
