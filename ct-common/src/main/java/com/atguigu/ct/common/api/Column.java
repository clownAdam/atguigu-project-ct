package com.atguigu.ct.common.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author Administrator
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * 列族
     * @return
     */
    String family() default "info";

    /**
     * 列名
     * @return
     */
    String column() default "";
}
