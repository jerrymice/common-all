package com.github.jerrymice.common.entity.entity;

import com.github.jerrymice.common.entity.code.GlobalErrorCode;

import java.io.Serializable;

/**
 * @author tumingjian
 * 创建时间: 2019-09-29 10:29
 * 功能说明:
 */
public interface Status extends Serializable {

    /**
     * 获取错误码,一般情况下,只有在isSuccess()返回false时,才会有错误码返回
     * @return 错误码
     */
    String getCode();

    /**
     * 返回错误消息,一般情况下,只有在isSuccess()返回false时,才会有消息返回
     * @return 返回结果附带的消息内容
     */
    String getMessage();

    /**
     * 状态是否成功
     * @return
     */
    default boolean isSuccess(){
        return GlobalErrorCode.REQUEST_SUCCESS.getCode().equals(getCode());
    }

    /**
     * 创建一个新的
     * @param code  相关错误代码
     * @param message 相关错误信息
     * @return
     */
    static Status wrapped(String code, String message){
        return new Status() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }

}
