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
public @interface RowKey {
}
