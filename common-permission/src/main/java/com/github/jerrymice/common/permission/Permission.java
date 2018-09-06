package com.github.jerrymice.common.permission;

import java.lang.annotation.*;

/**
 * @author tumingjian
 *
 * 说明:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Permissions.class)
@Documented
public @interface Permission {
    /**
     * 资源code.
     * @return
     */
    String[] value();

    /**
     * 资源名称
     * @return
     */
    String name() default "";

    /**
     * 资源备注
     * @return
     */
    String remark() default "";
}
