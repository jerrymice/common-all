package com.github.jerrymice.common.entity.annotation;

/**
 * @author tumingjian
 * 表mapper注解
 * comment为表字段注释的全部内容
 * remark为按照规则数据库注释规范处理后的内容.
 */
public @interface TableMapper {
    /**
     *
     * @return 表名
     */
    String tableName() default "";

    /**
     *
     * @return 表注释
     */
    String comment() default "";

    /**
     *
     * @return 主键个数
     */
    int parmaryKeyCount() default 1;
}
