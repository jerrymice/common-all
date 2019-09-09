package com.github.jerrymice.common.entity.entity;

import com.github.jerrymice.common.entity.annotation.ApiField;
import com.google.gson.annotations.Expose;

/**
 * @author tumingjian
 * 基础接口Result的默认实现,作为一般方法或是controller的最终返回值
 * @see Response
 */
public class ResultInfo<T> extends ResponseImpl<T> implements Result {
    @ApiField(comment = "执行结果返回true或false", jdbcType = "boolean")
    @Expose()
    protected boolean success;
    @ApiField(comment = "返回错误码,只有出错时才会有值", jdbcType = "int", length = 8)
    @Expose()
    protected String code;
    @ApiField(comment = "错误信息", jdbcType = "string", length = 128)
    @Expose()
    protected String message;
    @ApiField(comment = "返回请求的业务实体", jdbcType = "OBJECT")
    @Expose()
    protected T object;

    public ResultInfo() {
    }

    public ResultInfo(boolean isSuccess) {
        this.success = isSuccess;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public ResultInfo<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public ResultInfo<T> setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public ResultInfo<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ResultInfo<T> setObject(T object) {
        this.object = object;
        return this;
    }

}
