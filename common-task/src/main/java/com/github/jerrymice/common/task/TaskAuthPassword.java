package com.github.jerrymice.common.task;

/**
 * @author tumingjian
 *
 * 说明:用户名密码验证方式
 */
public interface TaskAuthPassword{
    /**
     * 验证用户名
     * @param username 用户名
     * @param password 密码
     * @return 返回验证结果true或false
     */
       boolean verify(String username, String password);
}
