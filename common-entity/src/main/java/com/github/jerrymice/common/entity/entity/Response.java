package com.github.jerrymice.common.entity.entity;

import java.io.Serializable;

/**
 * @author tumingjian
 * 基础接口,作为一般方法或是controller的最终返回值
 */
public interface Response<T> extends Serializable{


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
     * 返回业务数据,可以是任何业务数据
     * @return 返回结果中的业务对象
     */
    T getObject();
}
