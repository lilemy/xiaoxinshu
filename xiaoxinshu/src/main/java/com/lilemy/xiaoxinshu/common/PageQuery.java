package com.lilemy.xiaoxinshu.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.constant.StringConstant;
import com.lilemy.xiaoxinshu.exception.BusinessException;
import com.lilemy.xiaoxinshu.utils.SqlUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询实体类
 *
 * @author lilemy
 * @date 2025/06/03 18:27
 */
@Data
@Schema(name = "分页查询实体类", description = "分页查询实体类")
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 6095487855238622094L;

    /**
     * 分页大小
     */
    @Schema(description = "分页大小")
    private Integer pageSize;

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private Integer pageNum;

    /**
     * 排序列
     */
    @Schema(description = "排序列")
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @Schema(description = "排序方向")
    private String isAsc;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 构建分页对象
     */
    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        List<OrderItem> orderItems = buildOrderItem();
        if (CollUtil.isNotEmpty(orderItems)) {
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 构建排序
     * <p>
     * 支持的用法如下:
     * {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    private List<OrderItem> buildOrderItem() {
        if (StringUtils.isBlank(orderByColumn) || StringUtils.isBlank(isAsc)) {
            return null;
        }
        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        orderBy = StrUtil.toUnderlineCase(orderBy);

        // 兼容前端排序类型
        isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});

        String[] orderByArr = orderBy.split(StringConstant.SEPARATOR);
        String[] isAscArr = isAsc.split(StringConstant.SEPARATOR);
        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "排序参数有误");
        }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = orderByArr[i];
            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
            if ("asc".equals(isAscStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equals(isAscStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "排序参数有误");
            }
        }
        return list;
    }

}
