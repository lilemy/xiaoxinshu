package com.lilemy.xiaoxinshu.aop;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志拦截器
 *
 * @author lilemy
 * @date 2025-07-20 23:09
 */
@Slf4j
@Aspect
@Component
public class LogInterceptor {

    /**
     * 执行拦截
     */
    @Around("execution(* com.lilemy.xiaoxinshu.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String requestId = IdUtil.fastSimpleUUID();
        String path = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        String url = method + " " + path;
        String remoteHost = httpServletRequest.getRemoteHost();
        // 输出请求日志
        if (isJsonRequest(httpServletRequest)) {
            // 获取请求参数
            Object[] args = point.getArgs();
            String reqParam = Arrays.stream(args).map(JSONUtil::toJsonStr).collect(Collectors.joining(", "));
            log.info("开始请求 => ID:[{}], URL[{}], IP:[{}], 参数:[{}]", requestId, url, remoteHost, reqParam);
        } else {
            Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
            if (MapUtil.isNotEmpty(parameterMap)) {
                JSONObject finalJson = new JSONObject();
                parameterMap.forEach((key, valueArr) -> {
                    if (valueArr != null && valueArr.length > 0) {
                        String firstVal = valueArr[0];
                        // 尝试解析为 JSON，如果失败就当作普通字符串
                        if (JSONUtil.isTypeJSON(firstVal)) {
                            finalJson.set(key, JSONUtil.parse(firstVal));
                        } else {
                            finalJson.set(key, valueArr.length == 1 ? firstVal : valueArr);
                        }
                    }
                });
                log.info("开始请求 => ID:[{}], URL[{}], IP:[{}], 参数类型[param],参数:[{}]", requestId, url, remoteHost, finalJson);
            } else {
                log.info("开始请求 => ID:[{}], URL[{}], IP:[{}], 无参数", requestId, url, remoteHost);
            }
        }
        Object result = null;
        try {
            // 执行原方法
            result = point.proceed();
        } finally {
            // 输出响应日志
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getDuration().toMillis();
            if (result != null) {
                log.info("结束请求 => ID:[{}], 耗时:[{}]毫秒, 响应:[{}]", requestId, totalTimeMillis, JSONUtil.toJsonStr(result));
            } else {
                log.info("结束请求 => ID:[{}], 耗时:[{}]毫秒", requestId, totalTimeMillis);
            }
        }
        return result;
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request 请求
     * @return 是否为json
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return Strings.CI.startsWith(contentType, MediaType.APPLICATION_JSON_VALUE);
        }
        return false;
    }

}
