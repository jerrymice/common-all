package com.github.jerrymice.common.entity.code;

import java.text.MessageFormat;

/**
 * @author tumingjian
 * 全局错误码和错误信息
 */
public enum ErrorCode {
    /**
     * 未知错误
     */
    FAIL_UNKONW(-9999, "系统异常,未知错误"),
    /**
     * 用户未登录或登录已过期
     */
    USER_NO_LOGIN(-9998, "用户未登录,或登录信息已过期"),
    /**
     * 用户权限不足
     */
    USER_NO_PERMISSIONS(-9997, "用户权限不足"),
    /**
     * 系统忙,请稍后再试
     */
    SYSTEM_BUSYNESS(-9996, "系统忙,请稍后再试"),
    /**
     * 接口参数验证出错
     */
    PARAM_VERIFY_ERROR(-1, "接口参数验证出错"),
    /**
     * 定时任务:taskName或jobName定时任务名称不能为空
     */
    TASK_NAME_NOT_EMPTY(-9599, "taskName或jobName定时任务名称不能为空"),
    /**
     * 定时任务:定时任务用户验证失败
     */
    TASK_USER_AUTH_ERROR(-9598, "用户名:{0},无法通过身份认证"),
    /**
     * 定时任务:找不到指定的定时任务名称
     */
    TASK_NOT_FOUND(-9587, "找不到指定的定时任务名,taskName:{0},txid:{1}"),
    /**
     * 定时任务:定时任务执行失败
     */
    TASK_EXECUTE_ERROR(-9586, "定时任务执行失败,taskName:{0},txid:{1},exeception message:{2}");
    /**
     * 全局错误码
     */
    private int code;
    /**
     * 全局错误消息,可以是普通字符串,也可以是MessageFormat支持的模版信息
     */
    private String message;

    /**
     * 构造函数
     * @param code 全局错误码
     * @param message 全局错误消息
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取全局错误码
     * @return 错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取全局错误信息
     * @return 获取全局错误信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取全局错误信息,可以传入模版参数
     * @param params 模版参数
     * @return 经过MessageFormat.format()格式化后的信息
     * @see MessageFormat
     */
    public String getMessage(String... params) {
        return MessageFormat.format(message, params);
    }


}
