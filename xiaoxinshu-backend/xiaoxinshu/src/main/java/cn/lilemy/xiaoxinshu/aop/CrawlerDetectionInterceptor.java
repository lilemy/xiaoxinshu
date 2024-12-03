package cn.lilemy.xiaoxinshu.aop;

import cn.dev33.satoken.stp.StpUtil;
import cn.lilemy.xiaoxinshu.annotation.CrawlerDetection;
import cn.lilemy.xiaoxinshu.manager.CounterManager;
import cn.lilemy.xiaoxinshu.model.entity.User;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class CrawlerDetectionInterceptor {

    @Resource
    private CounterManager counterManager;

    @Resource
    private UserService userService;

    @Around("@annotation(crawlerDetection)")
    private Object doInterceptor(ProceedingJoinPoint joinPoint, CrawlerDetection crawlerDetection) throws Throwable {
        int warnCount = crawlerDetection.warnCount();
        int banCount = crawlerDetection.banCount();
        int timeInterval = crawlerDetection.timeInterval();
        TimeUnit timeUnit = crawlerDetection.timeUnit();
        int expirationTime = crawlerDetection.expirationTimeInSeconds();
        // 当前登录用户
        User loginUser = userService.getLoginUser();
        Long loginUserId = loginUser.getId();
        // 拼接访问 key
        String key = String.format("user:access:%s", loginUserId);
        long count = counterManager.incrAndGetCounter(key, timeInterval, timeUnit, expirationTime);
        // 是否封号
        if (count > banCount) {
            // 踢下线
            StpUtil.kickout(loginUserId);
            // 封号
            User updateUser = new User();
            updateUser.setId(loginUserId);
            updateUser.setUserRole("ban");
            userService.updateById(updateUser);
            throw new BusinessException(ResultCode.FORBIDDEN_ERROR, "访问太频繁，已被封号");
        }
        // 是否警告
        if (count == warnCount) {
            throw new BusinessException(ResultCode.FREQUENT_ERROR);
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
