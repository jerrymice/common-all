package com.github.jerrymice.common.entity.annotation;


import java.lang.annotation.*;

/**
 * @author tumingjian
 * 表字段注解
 * comment为表字段注释的全部内容
 * remark为按照规则数据库注释规范处理后的内容.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TableField {
    /**
     *
     * @return 表字段名称
     */
    String columnName() default "";

    /**
     *
     * @return 是否是主键
     */
    boolean isPrimaryKey() default false;

    /**
     *
     * @return 表字段注释全内容
     */
    String comment() default "";

    /**
     *
     * @return 表字段长度
     */
    double length() default 32;

    /**
     *
     * @return jdbc类型
     */
    String jdbcType() default "VARCHAR";
}
