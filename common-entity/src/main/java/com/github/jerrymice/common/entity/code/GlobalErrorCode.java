package com.github.jerrymice.common.entity.code;

import com.github.jerrymice.common.entity.entity.Status;

import java.text.MessageFormat;

/**
 * @author tumingjian
 * 全局错误码和错误信息
 */
public enum GlobalErrorCode implements Status {
    /**
     * 未知错误
     */
    REQUEST_SUCCESS("0000", "成功"),
    /**
     * 非法的或不能通过验证的参数.
     */
    INVALID_REQUEST_ARGUMENTS("0010", "非法的请求参数"),
    /**
     * 系统运行时有未知异常抛出时会显示这个错误
     */
    SYSTEM_UNKNOWN_ERROR("9999", "系统未知异常"),
    /**
     * 用户未授权或授权已过期
     */
    INVALID_SYSTEM_USER("0011", "用户未授权或授权已过期"),
    /**
     * 用户权限不足
     */
    INVALID_USER_PERMISSIONS("0012", "用户权限不足"),
    /**
     * 系统忙,请稍后再试
     */
    SYSTEM_BUSYNESS("9998", "系统忙,请稍后再试"),
    /**
     * 内部服务错误
     */
    SERVICE_ERROR("9997", "内部服务错误,内部服务调用失败或超时"),
    /**
     * 非法的服务API
     */
    INVALID_SERVICE_API("9996", "非法的服务API,找不到指定服务调用方法或URL");
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
     * @param code 全局错误码
     * @param message 全局错误消息
     */
     GlobalErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取全局错误码
     * @return 错误码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取全局错误信息
     * @return 获取全局错误信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 获取全局错误信息,可以传入模版参数
     * @param params 模版参数
     * @return 经过MessageFormat.format()格式化后的信息
     * @see MessageFormat
     */
    public Status format(String... params) {
        return Status.wrapped(this.code,MessageFormat.format(message, params));
    }

    public static Status getStatus(String code){
        GlobalErrorCode[] values = GlobalErrorCode.values();
        for(GlobalErrorCode globalErrorCode :values){
            if(globalErrorCode.getCode().equals(code)){
                return globalErrorCode;
            }
        }
        return null;
    }


}
