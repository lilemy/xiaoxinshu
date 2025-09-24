package com.lilemy.xiaoxinshu.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.xiaoxinshu.exception.BusinessException;
import com.lilemy.xiaoxinshu.utils.SqlUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询请求类
 *
 * @author lilemy
 * @date 2025-08-13 18:51
 */
@Data
@Schema(name = "PageQuery", description = "分页查询请求类")
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 2870834819465263127L;

    /**
     * 当前页号
     */
    @Schema(description = "当前页号")
    private Integer current;

    /**
     * 页面大小
     */
    @Schema(description = "页面大小")
    private Integer pageSize;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    @Schema(description = "排序顺序（默认降序）")
    private String sortOrder = "desc";

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认 20 条
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 构建分页对象
     */
    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getCurrent(), DEFAULT_PAGE_NUM);
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
     * {sortOrder:"asc",sortField:"id"} order by id asc
     * {sortOrder:"asc",sortField:"id,createTime"} order by id asc,create_time asc
     * {sortOrder:"desc",sortField:"id,createTime"} order by id desc,create_time desc
     * {sortOrder:"asc,desc",sortField:"id,createTime"} order by id asc,create_time desc
     * </p>
     */
    private List<OrderItem> buildOrderItem() {
        if (StringUtils.isBlank(sortField) || StringUtils.isBlank(sortOrder)) {
            return null;
        }
        String orderBy = SqlUtils.validSortField(sortField);
        orderBy = StrUtil.toUnderlineCase(orderBy);
        // 兼容前端排序类型
        sortOrder = StringUtils.replaceEach(sortOrder, new String[]{"ascend", "descend"}, new String[]{"asc", "desc"});
        String[] orderByArr = orderBy.split(",");
        String[] sortOrderArr = sortOrder.split(",");
        if (sortOrderArr.length != 1 && sortOrderArr.length != orderByArr.length) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "排序参数有误");
        }
        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = orderByArr[i];
            String sortOrderStr = sortOrderArr.length == 1 ? sortOrderArr[0] : sortOrderArr[i];
            if ("asc".equals(sortOrderStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equals(sortOrderStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "排序参数有误");
            }
        }
        return list;
    }

    public PageQuery(Integer pageSize, Integer current) {
        this.pageSize = pageSize;
        this.current = current;
    }
}
