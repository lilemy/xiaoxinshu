package cn.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.mapper.InterfaceInfoMapper;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import cn.lilemy.xiaoxinshu.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshu.model.enums.InterfaceEnum;
import cn.lilemy.xiaoxinshu.model.enums.InterfaceInfoStatusEnum;
import cn.lilemy.xiaoxinshu.model.vo.InterfaceInfoVO;
import cn.lilemy.xiaoxinshu.service.InterfaceInfoService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshu.util.SqlUtils;
import cn.lilemy.xiaoxinshuclientsdk.client.XiaoxinshuClient;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qq233
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private XiaoxinshuClient xiaoxinshuClient;

    @Resource
    private UserService userService;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        ThrowUtils.throwIf(interfaceInfo == null, ResultCode.PARAMS_ERROR);
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String actualUrl = interfaceInfo.getActualUrl();
        String path = interfaceInfo.getPath();
        String method = interfaceInfo.getMethod();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ResultCode.PARAMS_ERROR, "接口名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(url), ResultCode.PARAMS_ERROR, "接口地址不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(actualUrl), ResultCode.PARAMS_ERROR, "接口实际地址不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(path), ResultCode.PARAMS_ERROR, "接口路径不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(method), ResultCode.PARAMS_ERROR, "接口方法不能为空");
        }
        if (StringUtils.isBlank(name)) {
            ThrowUtils.throwIf(name.length() > 80, ResultCode.PARAMS_ERROR, "接口名称过长");
        }
        if (StringUtils.isBlank(description)) {
            ThrowUtils.throwIf(name.length() > 200, ResultCode.PARAMS_ERROR, "接口描述过长");
        }
    }

    @Override
    public Long addInterfaceInfo(InterfaceInfoAddRequest addRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(addRequest, interfaceInfo);
        // 数据校验
        this.validInterfaceInfo(interfaceInfo, true);
        // 添加默认值
        User loginUser = userService.getLoginUser();
        interfaceInfo.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return interfaceInfo.getId();
    }

    @Override
    public boolean updateInterfaceInfo(InterfaceInfoUpdateRequest updateRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(updateRequest, interfaceInfo);
        // 数据校验
        this.validInterfaceInfo(interfaceInfo, false);
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(updateRequest.getId());
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        return this.updateById(interfaceInfo);
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        return interfaceInfoVO;
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = interfaceQueryRequest.getId();
        String name = interfaceQueryRequest.getName();
        String description = interfaceQueryRequest.getDescription();
        Long notId = interfaceQueryRequest.getNotId();
        String searchText = interfaceQueryRequest.getSearchText();
        String url = interfaceQueryRequest.getUrl();
        String actualUrl = interfaceQueryRequest.getActualUrl();
        String path = interfaceQueryRequest.getPath();
        String requestParams = interfaceQueryRequest.getRequestParams();
        String requestHeader = interfaceQueryRequest.getRequestHeader();
        String responseHeader = interfaceQueryRequest.getResponseHeader();
        Integer status = interfaceQueryRequest.getStatus();
        String method = interfaceQueryRequest.getMethod();
        Long userId = interfaceQueryRequest.getUserId();
        String sortField = interfaceQueryRequest.getSortField();
        String sortOrder = interfaceQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("name", name).or().like("description", description));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.like(StringUtils.isNotBlank(actualUrl), "actualUrl", actualUrl);
        queryWrapper.like(StringUtils.isNotBlank(path), "path", path);
        queryWrapper.like(StringUtils.isNotBlank(requestParams), "requestParams", requestParams);
        queryWrapper.like(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
        queryWrapper.like(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
        queryWrapper.like(StringUtils.isNotBlank(method), "method", method);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(this::getInterfaceInfoVO).toList();
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public Boolean onlineInterface(Long id) {
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR, "接口不存在");
        // 判断接口是否可以调用
        String image = xiaoxinshuClient.getRandomImage();
        ThrowUtils.throwIf(StringUtils.isBlank(image), ResultCode.SYSTEM_ERROR, "当前接口不可调用");
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return this.updateById(interfaceInfo);
    }

    @Override
    public Boolean offlineInterface(Long id) {
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.PARAMS_ERROR, "接口不存在");
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        return this.updateById(interfaceInfo);
    }

    @Override
    public Object invokeInterface(InterfaceInfoInvokeRequest invokeRequest) {
        // 1. 校验接口是否存在
        Long id = invokeRequest.getId();
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        User loginUser = userService.getLoginUser();
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        XiaoxinshuClient loginUserClient = new XiaoxinshuClient(accessKey, secretKey);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        // 2. 只有已经上线的接口才能调用
        ThrowUtils.throwIf(oldInterfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue(), ResultCode.NO_AUTH_ERROR, "该接口已关闭");
        // 3. 调用接口
        if (oldInterfaceInfo.getPath().equals(InterfaceEnum.IMAGE_RANDOM.getValue())) {
            String params = invokeRequest.getUserRequestParams();
            if (StringUtils.isNotBlank(params)) {
                return loginUserClient.getRandomImage(params);
            } else {
                return loginUserClient.getRandomImage();
            }
        } else {
            throw new BusinessException(ResultCode.NOT_FOUND_ERROR, "暂无该接口");
        }
    }
}




