package com.longlong.common.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableJoin {

    /**
     * 连表方式 left  right INNER
     */
    Join join() default Join.LEFT_JOIN;

    /**
     * 当前字段名称
     */
    String name() ;

    /**
     * 连表对应的字段
     */
    String fieldName();

    /**
     * 对应的表名(实体类)
     */
    Class<?> tableName();
}
