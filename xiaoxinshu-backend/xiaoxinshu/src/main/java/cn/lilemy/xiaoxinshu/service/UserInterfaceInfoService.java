package cn.lilemy.xiaoxinshu.service;

import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author qq233
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 添加用户接口关系
     *
     * @param addRequest 添加用户接口关系请求体
     * @return 新关系 id
     */
    Long addUserInterfaceInfo(UserInterfaceInfoAddRequest addRequest);

    /**
     * 更新用户接口调用次数
     *
     * @param updateRequest 用户接口调用次数请求体
     * @return 是否操作成功
     */
    Boolean updateUserInterfaceInfoLeftNum(UserInterfaceInfoUpdateRequest updateRequest);

    /**
     * 获取分页查询条件
     *
     * @param queryRequest 用户接口关系查询请求体
     * @return 分页请求
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest queryRequest);
}
