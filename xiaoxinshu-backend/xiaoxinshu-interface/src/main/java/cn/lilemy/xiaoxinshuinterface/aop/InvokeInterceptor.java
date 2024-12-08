package cn.lilemy.xiaoxinshuinterface.aop;

import cn.lilemy.xiaoxinshuclientsdk.utils.SignUtils;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshucommon.exception.ThrowUtils;
import cn.lilemy.xiaoxinshucommon.model.entity.InterfaceInfo;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshuservice.service.InnerInterfaceInfoService;
import cn.lilemy.xiaoxinshuservice.service.InnerUserInterfaceInfoService;
import cn.lilemy.xiaoxinshuservice.service.InnerUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 接口调用 AOP
 **/
@Aspect
@Component
@Slf4j
public class InvokeInterceptor {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    /**
     * 执行拦截
     */
    @Around("execution(* cn.lilemy.xiaoxinshuinterface.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 从请求头中获取用户和接口信息 id
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // 用户鉴权（判断 ak、sk 是否合法）
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(invokeUser == null, ResultCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(nonce == null || Long.parseLong(nonce) > 10000L, ResultCode.PARAMS_ERROR);
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;
        ThrowUtils.throwIf(timestamp == null || (currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES, ResultCode.PARAMS_ERROR);
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey);
        ThrowUtils.throwIf(sign == null || !sign.equals(serverSign), ResultCode.PARAMS_ERROR);
        // 请求的模拟接口是否存在，以及请求方法是否匹配
        InterfaceInfo interfaceInfo;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(requestURI, method);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(interfaceInfo == null, ResultCode.PARAMS_ERROR);
        // 判断用户是否还有调用次数
        Long interfaceInfoId = interfaceInfo.getId();
        Long userId = invokeUser.getId();
        Integer leftCount;
        try {
            leftCount = innerUserInterfaceInfoService.invokeLeftCount(interfaceInfoId, userId);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        ThrowUtils.throwIf(leftCount <= 0, ResultCode.NO_AUTH_ERROR, "该接口调用次数用完");
        // 执行原方法
        Object result = point.proceed();
        // 接口调用成功扣减用户调用次数
        innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
        return result;
    }
}
