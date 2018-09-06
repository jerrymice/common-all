package com.github.jerrymice.common.entity.annotation;


import java.lang.annotation.*;

/**
 * @author tumingjian
 * 标注字段注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ApiField {
    /**
     * 默认值 null
     */
    String DEFAULT_NAME = "__null__";

    /**
     * 字段中文名,为空时取字段名
     *
     * @return 返回字段名
     */
    String name() default DEFAULT_NAME;

    /**
     * @return 返回字段的注释内容
     */
    String comment() default "";

    /**
     * @return 返回字段的JDBC类型(如果有)
     */
    String jdbcType() default "";

    /**
     *
     *
     * @return 返回字段长度
     */
    double length() default 8d;

}
