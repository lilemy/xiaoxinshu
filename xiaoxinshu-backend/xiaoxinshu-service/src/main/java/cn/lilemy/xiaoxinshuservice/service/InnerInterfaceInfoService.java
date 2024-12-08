package cn.lilemy.xiaoxinshuservice.service;


import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;

/**
 * 内部接口信息服务
 */
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
