package cn.lilemy.xiaoxinshuservice.service;

/**
 * 内部用户接口信息服务
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return 是否调用成功
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 判断用户是否还有调用次数
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return 剩余调用次数
     */
    Integer invokeLeftCount(Long interfaceInfoId, Long userId);
}

