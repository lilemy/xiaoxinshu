package cn.lilemy.xiaoxinshu.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.lilemy.xiaoxinshu.constant.CommonConstant;
import cn.lilemy.xiaoxinshu.mapper.UserInterfaceInfoMapper;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import cn.lilemy.xiaoxinshu.service.InterfaceInfoService;
import cn.lilemy.xiaoxinshu.service.UserInterfaceInfoService;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshu.util.SqlUtils;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshucommon.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * @author qq233
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public Long addUserInterfaceInfo(UserInterfaceInfoAddRequest addRequest) {
        Long userId = addRequest.getUserId();
        Long interfaceInfoId = addRequest.getInterfaceInfoId();
        Integer leftNum = addRequest.getLeftNum();
        ThrowUtils.throwIf(userId == null || userId <= 0, ResultCode.PARAMS_ERROR, "添加用户非法");
        ThrowUtils.throwIf(interfaceInfoId == null || interfaceInfoId <= 0, ResultCode.PARAMS_ERROR, "添加接口信息非法");
        ThrowUtils.throwIf(leftNum < 0 || leftNum > 10000, ResultCode.PARAMS_ERROR, "调用次数非法");
        // 判断对应用户和接口信息是否存在
        User user = userService.getById(userId);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR, "添加用户不存在");
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        ThrowUtils.throwIf(interfaceInfo == null, ResultCode.NOT_FOUND_ERROR, "添加接口不存在");
        // 查询对应数据是否存在
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("interface_info_id", interfaceInfoId);
        long count = this.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ResultCode.PARAMS_ERROR, "这条记录已存在");
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
        userInterfaceInfo.setLeftNum(leftNum);
        boolean save = this.save(userInterfaceInfo);
        ThrowUtils.throwIf(!save, ResultCode.OPERATION_ERROR);
        return userInterfaceInfo.getId();
    }

    @Override
    public Boolean updateUserInterfaceInfoLeftNum(UserInterfaceInfoUpdateRequest updateRequest) {
        Long id = updateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResultCode.PARAMS_ERROR);
        // 查询是否存在
        UserInterfaceInfo oldUserInterfaceInfo = this.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        Integer leftNum = updateRequest.getLeftNum();
        ThrowUtils.throwIf(leftNum < 0 || leftNum > 10000, ResultCode.PARAMS_ERROR, "调用次数非法");
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("left_num", leftNum);
        boolean update = this.update(updateWrapper);
        ThrowUtils.throwIf(!update, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest queryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = queryRequest.getId();
        Long userId = queryRequest.getUserId();
        Long interfaceInfoId = queryRequest.getInterfaceInfoId();
        Integer status = queryRequest.getStatus();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(interfaceInfoId), "interface_info_id", interfaceInfoId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }
}




