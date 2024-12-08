package cn.lilemy.xiaoxinshu.service.impl.inner;

import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshuservice.service.InnerUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public User getInvokeUser(String accessKey) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(accessKey), ResultCode.PARAMS_ERROR);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("access_key", accessKey);
        return userService.getOne(queryWrapper);
    }
}
