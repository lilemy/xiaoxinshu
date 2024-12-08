package cn.lilemy.xiaoxinshu.aop;

import cn.lilemy.xiaoxinshu.annotation.AuthCheck;
import cn.lilemy.xiaoxinshucommon.model.entity.User;
import cn.lilemy.xiaoxinshu.model.enums.UserRoleEnum;
import cn.lilemy.xiaoxinshu.service.UserService;
import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

//@Aspect
//@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        UserRoleEnum userRole = UserRoleEnum.getEnumByValue(mustRole);
        // 当前登录用户
        User loginUser = userService.getLoginUser();
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        // 不需要权限，放行
        if (userRole == null) {
            return joinPoint.proceed();
        }
        // 如果被封号，直接拒绝
        if (UserRoleEnum.BAN.equals(userRoleEnum)) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        // 必须有管理员权限
        if (UserRoleEnum.ADMIN.equals(userRole)) {
            // 用户没有管理员权限，拒绝
            if (!UserRoleEnum.ADMIN.equals(userRoleEnum)) {
                throw new BusinessException(ResultCode.NO_AUTH_ERROR);
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }

}
