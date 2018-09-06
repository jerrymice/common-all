package org.open.code.base.task;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * 定时任务标记
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {
    /**
     *
     * @return 任务名
     */
    String value() default "";

    /**
     *
     * @return 任务超时时间
     */
    int timeout() default  60; //任务超时时间,单位秒

    /**
     *
     * @return 任务备注
     */
    String remark() default "";
}