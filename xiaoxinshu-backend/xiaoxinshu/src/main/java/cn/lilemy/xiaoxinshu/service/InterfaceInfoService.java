package cn.lilemy.xiaoxinshu.service;

import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;
import cn.lilemy.xiaoxinshu.model.vo.InterfaceInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author qq233
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验数据
     *
     * @param interfaceInfo 接口信息
     * @param add           对创建的数据进行校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 添加接口
     *
     * @param addRequest 添加接口请求体
     * @return 添加接口 id
     */
    Long addInterfaceInfo(InterfaceInfoAddRequest addRequest);

    /**
     * 更新接口
     *
     * @param updateRequest 更新接口请求体
     * @return 是否更新成功
     */
    boolean updateInterfaceInfo(InterfaceInfoUpdateRequest updateRequest);

    /**
     * 获取接口信息封装
     *
     * @param interfaceInfo 接口信息
     * @return 接口信息封装
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);

    /**
     * 获取接口信息查询条件
     *
     * @param interfaceQueryRequest 接口信息查询
     * @return 接口查询条件
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceQueryRequest);

    /**
     * 获取接口信息分页封装
     *
     * @param interfaceInfoPage 接口信息分页
     * @return 接口信息分页封装
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage);

    /**
     * 上线接口
     *
     * @param id 接口 id
     * @return 是否上线成功
     */
    Boolean onlineInterface(Long id);

    /**
     * 下线接口
     *
     * @param id 接口 id
     * @return 是否下线成功
     */
    Boolean offlineInterface(Long id);

    /**
     * 在线调用接口
     *
     * @param invokeRequest 调用接口请求体
     * @return 接口返回信息
     */
    Object invokeInterface(InterfaceInfoInvokeRequest invokeRequest);
}
