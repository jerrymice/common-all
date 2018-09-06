package com.github.jerrymice.common.permission;

/**
 * @author tumingjian
  *
 * 说明:获取当前用户
 */
public interface PermissionUserHandler<T> {
    /**
     * 获取当前用户
     * @return 返回当前用户
     */
    T getCurrentUser();
}
