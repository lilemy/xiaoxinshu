package cn.lilemy.xiaoxinshu.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口信息更新请求
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4419259702928187898L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 实际接口地址
     */
    private String actualUrl;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求类型
     */
    private String method;

}
