package com.lilemy.xiaoxinshu.manager.rustfs.entity;

import lombok.Data;

/**
 * 对象存储信息
 *
 * @author lilemy
 * @date 2026-01-08 16:38
 */
@Data
public class OssVo {

    /**
     * 对象存储地址
     */
    private String url;

    /**
     * 对象存储大小
     */
    private Long size;
}
