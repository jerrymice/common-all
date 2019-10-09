package com.github.jerrymice.common.entity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

/**
 * @author tumingjian
 * 基础接口,作为一般方法或是controller的最终返回值
 */
public interface Body<T> {
    /**
     * 返回业务数据,可以是任何业务数据
     *
     * @return 返回结果中的业务对象
     */
    T getBody();

    /**
     * body值是否为空
     *
     * @return
     */
    @JsonIgnore
    default boolean isEmpty() {
        if (getBody() == null) {
            return true;
        }else if (getBody() instanceof Collection) {
            return ((Collection) getBody()).isEmpty();
        }else{
            return false;
        }
    }
}
