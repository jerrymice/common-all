package com.github.jerrymice.common.entity.entity;

import com.github.jerrymice.common.entity.annotation.ApiField;
import com.google.gson.annotations.Expose;

/**
 * @author tumingjian
 * 创建时间: 2019-09-08 12:22
 * 功能说明:
 */
public class ResponseImpl<T> implements Response {
    @Expose()
    protected String code;
    @ApiField(comment="错误信息",jdbcType="string",length=128)
    @Expose()
    protected String message;
    @ApiField(comment="返回请求的业务实体",jdbcType="OBJECT")
    @Expose()
    protected T object;

    @Override
    public String getCode() {
        return code;
    }

    public ResponseImpl<T> setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ResponseImpl<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public T getObject() {
        return object;
    }

    public ResponseImpl<T> setObject(T object) {
        this.object = object;
        return this;
    }
}
