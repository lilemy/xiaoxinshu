package cn.lilemy.xiaoxinshu.service.impl.inner;

import cn.lilemy.xiaoxinshu.service.UserInterfaceInfoService;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.UserInterfaceInfo;
import cn.lilemy.xiaoxinshuservice.service.InnerUserInterfaceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 调用成功并更新次数
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interface_info_id", interfaceInfoId);
        updateWrapper.eq("user_id", userId);
        updateWrapper.setSql("left_num = left_num - 1, total_num = total_num + 1");
        return userInterfaceInfoService.update(updateWrapper);
    }

    @Override
    public Integer invokeLeftCount(Long interfaceInfoId, Long userId) {
        // 判断
        ThrowUtils.throwIf(interfaceInfoId <= 0 || userId <= 0, ResultCode.PARAMS_ERROR);
        // 判断对应记录是否存在
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("interface_info_id", interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        ThrowUtils.throwIf(userInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR, "无此记录");
        // 剩余调用次数
        return userInterfaceInfo.getLeftNum();
    }
}
