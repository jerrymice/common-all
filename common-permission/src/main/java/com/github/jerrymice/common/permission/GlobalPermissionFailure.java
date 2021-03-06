package com.github.jerrymice.common.permission;




import com.github.jerrymice.common.entity.code.GlobalErrorCode;
import com.github.jerrymice.common.entity.entity.ResultInfo;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author tumingjian
 *
 * 说明: 这个接口用来扩展用户没有权限时的返回值.
 */
public interface GlobalPermissionFailure {
    /**
     * 权限验证失败时的处理逻辑
     *
     * @param joinPoint
     * @param permissions
     * @return
     */
    default Object permissionFailure(ProceedingJoinPoint joinPoint, Permission[] permissions) {
        return new ResultInfo<>(false).setCode(GlobalErrorCode.INVALID_USER_PERMISSIONS.getCode()).setMessage(GlobalErrorCode.INVALID_USER_PERMISSIONS.getMessage());
    }
}
