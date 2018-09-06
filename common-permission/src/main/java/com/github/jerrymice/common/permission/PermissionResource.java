package com.github.jerrymice.common.permission;

import java.util.Set;

/**
 * @author tumingjian
 *
 * 说明:这个接口用来自定义获取用户当前所允许的资源
 */
public interface PermissionResource<T> {
    /**
     * 获取当前用户所允许的资源
     * @param user 当前用户
     * @return  返回用户允许的资源code列表
     */
    Set<String> currentUserResources(T user);
}
