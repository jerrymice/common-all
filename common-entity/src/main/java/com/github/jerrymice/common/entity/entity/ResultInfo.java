package com.github.jerrymice.common.entity.entity;

import com.github.jerrymice.common.entity.annotation.ApiField;
import com.github.jerrymice.common.entity.code.GlobalErrorCode;
import com.google.gson.annotations.Expose;

/**
 * @author tumingjian
 * 基础接口Result的默认实现,作为一般方法或是controller的最终返回值
 * @see Body
 */
public class ResultInfo<T> implements Result<T> {
    @ApiField(comment = "返回错误码,正常返回0000", jdbcType = "string", length = 4)
    @Expose()
    protected String code;
    @ApiField(comment = "错误信息", jdbcType = "string", length = 128)
    @Expose()
    protected String message;
    @ApiField(comment = "返回请求的业务实体", jdbcType = "object")
    @Expose()
    protected T body;

    public ResultInfo() {
    }
    public ResultInfo(boolean success) {
        if(success){
                this.code= GlobalErrorCode.REQUEST_SUCCESS.getCode();
                this.message= GlobalErrorCode.REQUEST_SUCCESS.getMessage();
        }
    }

    public ResultInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultInfo(Status status) {
        this.code = status.getCode();
        this.message=status.getMessage();
    }
    public ResultInfo(T body) {
        this(GlobalErrorCode.REQUEST_SUCCESS);
        this.body = body;
    }

    public ResultInfo<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public ResultInfo<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResultInfo<T> setBody(T body) {
        this.body = body;
        return this;
    }

    @Override
    public T getBody() {
        return this.body;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
