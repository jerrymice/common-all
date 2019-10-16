package com.github.jerrymice.common.entity.ex;

import com.github.jerrymice.common.entity.entity.Status;

/**
 * @author tumingjian
 * 创建时间: 2019-10-16 12:34
 * 功能说明:这是一个专门用于检查参数或相关结果是否正确的异常类.
 * 在Http Rest API中会统一将这类异常的HttpStatus变更为400.
 */
public class ResultIllegalArgumentException extends ResultException {
    public ResultIllegalArgumentException() {
        super();
    }

    public ResultIllegalArgumentException(String message) {
        super(message);
    }

    public ResultIllegalArgumentException(Status status) {
        super(status);
    }

    public ResultIllegalArgumentException(String code, String message) {
        super(code, message);
    }

    public ResultIllegalArgumentException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ResultIllegalArgumentException(Status status, Throwable cause) {
        super(status, cause);
    }

    public ResultIllegalArgumentException(String code, Throwable cause) {
        super(code, cause);
    }

    public ResultIllegalArgumentException(Throwable cause, String message) {
        super(cause, message);
    }

    public ResultIllegalArgumentException(ResultException cause) {
        super(cause);
    }
}
