package com.github.jerrymice.common.entity.code;

import com.github.jerrymice.common.entity.entity.Status;

import java.text.MessageFormat;

/**
 * @author tumingjian
 * 全局错误码和错误信息
 */
public enum GlobalErrorCode implements Status {
    /**
     * 成功
     */
    REQUEST_SUCCESS_ONLY_CODE("0000", null),
    /**
     * 成功
     */
    REQUEST_SUCCESS("0000", "成功"),
    /**
     * 非法的或不能通过验证的参数.
     */
    INVALID_REQUEST_ARGUMENTS("0010", "非法的请求参数"),
    /**
     * 用户未授权或授权已过期
     */
    INVALID_USER_SESSION("0011", "用户未授权或授权已过期"),
    /**
     * 用户未授权或授权已过期
     */
    INVALID_USER_LOGIN("0012", "登录失败,用户名或密码错误"),
    /**
     * 用户权限不足
     */
    INVALID_USER_PERMISSIONS("0020", "用户权限不足"),
    /**
     * 系统忙,请稍后再试
     */
    SYSTEM_BUSYNESS("9998", "系统忙,请稍后再试"),
    /**
     * 内部服务错误
     */
    SERVICE_ERROR("9997", "内部服务调用失败或超时"),
    /**
     * 非法的服务API
     */
    INVALID_SERVICE_API("9996", "找不到指定服务调用方法或URL"),
    /**
     * 系统运行时有未知异常抛出时会显示这个错误
     */
    UNKNOWN_SYSTEM_ERROR("9999", "未知系统异常");
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

    /**
     * 使用相同的代码,但生成不同的消息
     * @param message
     * @return
     */
    public Status message(String message){
        return Status.wrapped(this.code,message);
    }

    public Status getStatus(){
        return Status.wrapped(this.code,this.message);
    }
}
