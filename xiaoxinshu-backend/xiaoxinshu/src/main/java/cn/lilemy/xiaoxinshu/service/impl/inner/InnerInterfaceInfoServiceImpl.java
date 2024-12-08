package cn.lilemy.xiaoxinshu.service.impl.inner;

import cn.lilemy.xiaoxinshu.service.InterfaceInfoService;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;
import cn.lilemy.xiaoxinshuservice.service.InnerInterfaceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(path, method), ResultCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path", path);
        queryWrapper.eq("method", method);
        return interfaceInfoService.getOne(queryWrapper);
    }
}
