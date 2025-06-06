package com.lilemy.xiaoxinshu.common;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页数据对象
 *
 * @author lilemy
 * @date 2025/06/03 18:27
 */
@Data
@NoArgsConstructor
@Schema(name = "TableDataInfo", description = "分页数据对象")
public class TableDataInfo<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3981039519079424068L;

    /**
     * 消息状态码
     */
    @Schema(description = "消息状态码")
    private int code;

    /**
     * 列表数据
     */
    @Schema(description = "列表数据")
    private List<T> data;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String message;

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private Integer pageNum;

    /**
     * 分页大小
     */
    @Schema(description = "分页大小")
    private Integer pageSize;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private long total;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total, Integer pageNum, Integer pageSize) {
        this.data = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.code = HttpStatus.HTTP_OK;
        this.message = "查询成功";
    }

    /**
     * 根据分页对象构建表格分页数据对象
     */
    public static <T> TableDataInfo<T> build(IPage<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(ResultCode.SUCCESS.getCode());
        rspData.setMessage("查询成功");
        rspData.setData(page.getRecords());
        rspData.setTotal(page.getTotal());
        rspData.setPageNum((int) page.getCurrent());
        rspData.setPageSize((int) page.getSize());
        return rspData;
    }

    /**
     * 根据数据列表构建表格分页数据对象
     */
    public static <T> TableDataInfo<T> build(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(ResultCode.SUCCESS.getCode());
        rspData.setMessage("查询成功");
        rspData.setData(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 构建表格分页数据对象
     */
    public static <T> TableDataInfo<T> build() {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(ResultCode.SUCCESS.getCode());
        rspData.setMessage("查询成功");
        return rspData;
    }

}
