package com.github.jerrymice.common.task;

/**
 * @author tumingjian
 *
 * 说明: token认证方式
 */
public interface TaskAuthToken{
    /**
     * 验证token
     * @param token 要验证的token
     * @return 返回验证结果true或false
     */
   boolean verify(String token);
}
