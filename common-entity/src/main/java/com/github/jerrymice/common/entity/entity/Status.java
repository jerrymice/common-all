package com.github.jerrymice.common.entity.entity;

/**
 * @author tumingjian
 * 创建时间: 2019-09-08 12:11
 * 功能说明:
 */
public interface Status {
    /**
     * 请求是否成功.一般异常时返回true,有时候也可以当作某个条件判断的结果,或是某个方法的前置检查结果.
     * @return 返回ture或false
     */
    boolean isSuccess();
}
