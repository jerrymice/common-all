package com.github.jerrymice.common.permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.util.Arrays;
import java.util.Set;

/**
 * @author tumingjian
 *
 * 说明:权限控制的切面,这个切面的优先级应该是所有切面中最高的.
 */
@Aspect
public class PermissionAspect {


    private GlobalPermissionFailure permissionFailure;

    private PermissionResource permissionResource;

    private PermissionUserHandler handler;

    public PermissionAspect(GlobalPermissionFailure permissionFailure, PermissionResource permissionResource, PermissionUserHandler handler) {
        this.permissionFailure = permissionFailure;
        this.permissionResource = permissionResource;
        this.handler = handler;
    }

    public PermissionAspect() {
    }

    public GlobalPermissionFailure getPermissionFailure() {
        return permissionFailure;
    }

    public void setPermissionFailure(GlobalPermissionFailure permissionFailure) {
        this.permissionFailure = permissionFailure;
    }

    public PermissionResource getPermissionResource() {
        return permissionResource;
    }

    public void setPermissionResource(PermissionResource permissionResource) {
        this.permissionResource = permissionResource;
    }

    public PermissionUserHandler getHandler() {
        return handler;
    }

    public void setHandler(PermissionUserHandler handler) {
        this.handler = handler;
    }

    @Pointcut(value = "@annotation(com.github.jerrymice.common.permission.Permissions)")
    public void pointCuts() {

    }

    @Pointcut(value = "@annotation(com.github.jerrymice.common.permission.Permission)")
    public void pointCut() {

    }

    /**
     * aop注入.权限控制器
     * @param joinPoint 切入点
     * @return 返回函数的执行结果
     * @throws Throwable 异常信息
     */
    @Around(value = "pointCut() && @annotation(permission)")
    public Object round(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        return roundProcess(joinPoint, new Permission[]{permission});
    }

    /**
     * aop注入.权限控制器
     *
     * @param joinPoint 切入点
     * @return 返回函数的执行结果
     * @throws Throwable 异常信息
     */
    @Around(value = "pointCuts() && @annotation(permissions)")
    public Object around(ProceedingJoinPoint joinPoint, Permissions permissions) throws Throwable {
        Permission[] list = permissions.value();
        return roundProcess(joinPoint, list);
    }

    /**
     * 切入运行函数
     * @param joinPoint 切入点
     * @param list 注解列表
     * @return  函数返回值
     * @throws Throwable  异常信息
     */
    private Object roundProcess(ProceedingJoinPoint joinPoint, Permission[] list) throws Throwable {
        Set<String> resources = permissionResource.currentUserResources(handler.getCurrentUser());
        boolean isPermission = false;
        if (resources!=null && resources.size()>0) {
            for (Permission per : list) {
                String[] value = per.value();
                isPermission = resources.containsAll(Arrays.asList(value));
                if (isPermission) {
                    break;
                }
            }
        }
        if (!isPermission) {
            Object o = permissionFailure.permissionFailure(joinPoint, list);
            return o;
        } else {
            if (joinPoint.getArgs() == null || joinPoint.getArgs().length == 0) {
                Object proceed = joinPoint.proceed();
                return proceed;
            } else {
                Object proceed = joinPoint.proceed(joinPoint.getArgs());
                return proceed;
            }
        }
    }

}
