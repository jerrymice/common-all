package com.github.jerrymice.common.task;

import com.github.jerrymice.common.entity.entity.Status;

import java.text.MessageFormat;

/**
 * @author tumingjian
 * 创建时间: 2019-09-29 14:39
 * 功能说明:
 */
public enum TaskErrorCode implements Status {

    /**
     * 定时任务:taskName或jobName定时任务名称不能为空
     */
    TASK_NAME_NOT_EMPTY("0101", "taskName或jobName定时任务名称不能为空"),
    /**
     * 定时任务:定时任务用户验证失败
     */
    TASK_USER_AUTH_ERROR("0102", "用户名:{0},无法通过身份认证"),
    /**
     * 定时任务:找不到指定的定时任务名称
     */
    TASK_NOT_FOUND("0103", "找不到指定的定时任务名,taskName:{0},txid:{1}"),
    /**
     * 定时任务:定时任务执行失败
     */
    TASK_EXECUTE_ERROR("0104", "定时任务执行失败,taskName:{0},txid:{1},exeception message:{2}"),
    /**
     * 定时任务:任务必要参数不能为空
     */
    TASK_INVALID_ARGUMENTS("0105", "定时任务执行失败,taskName:{0},txid:{1},{2}参数不能为空"),
    /**
     * 定时任务:任务必要参数不能为空
     */
    TASK_EXECUTE_TIMEOUT("0106", "定时任务执行失败,taskName:{0},txid:{1},任务执行超时");
    /**
     * 全局错误码
     */
    private String code;
    /**
     * 全局错误消息,可以是普通字符串,也可以是MessageFormat支持的模版信息
     */
    private String message;

    /**
     * 构造函数
     *
     * @param code    全局错误码
     * @param message 全局错误消息
     */
    TaskErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取全局错误码
     *
     * @return 错误码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取全局错误信息
     *
     * @return 获取全局错误信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 获取全局错误信息,可以传入模版参数
     *
     * @param params 模版参数
     * @return 经过MessageFormat.format()格式化后的信息
     * @see MessageFormat
     */
    public Status format(String... params) {
        return Status.wrapped(this.code, MessageFormat.format(message, params));
    }
}
