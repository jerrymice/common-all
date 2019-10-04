package com.github.jerrymice.common.entity.ex;

import com.github.jerrymice.common.entity.code.GlobalErrorCode;
import com.github.jerrymice.common.entity.entity.Status;

/**
 * @author tumingjian
 * 创建时间: 2019-09-29 09:31
 * 功能说明: 一个本地业务异常,使用者可以自定义抛出这个异常.
 */
public class ResultException extends RuntimeException {
    private String code ;

    public ResultException() {
    }

    public ResultException(String message) {
        super(message);
        this.code = GlobalErrorCode.SYSTEM_UNKNOWN_ERROR.getCode();
    }

    public ResultException(Status status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public ResultException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ResultException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ResultException(Status status, Throwable cause) {
        super(status.getMessage(), cause);
        this.code = status.getCode();
    }

    public ResultException(String code, Throwable cause) {
        super(GlobalErrorCode.getStatus(code)!=null? GlobalErrorCode.getStatus(code).getMessage():cause.getLocalizedMessage(),cause);
        this.code = code;
    }
    public ResultException(Throwable cause,String message) {
        super(message, cause);
        this.code = GlobalErrorCode.SYSTEM_UNKNOWN_ERROR.getCode();
    }
    public ResultException(ResultException cause) {
        super(cause);
        this.code=cause.getCode();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + "[" + this.code + "]" + message) : (s + ": " + "[" + this.code + "]");
    }
}
