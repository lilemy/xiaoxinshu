package cn.lilemy.xiaoxinshuservice.service;


import cn.lilemy.xiaoxinshucommon.model.entity.User;

/**
 * 内部用户服务
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     *
     * @param accessKey accessKey
     * @return 用户信息
     */
    User getInvokeUser(String accessKey);
}
