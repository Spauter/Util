package com.bloducspauter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel表格单元格注解工具
 * @author Bloduc Spauter
 * @version 1.18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {
    /**
     * 单元格真正的名字
     * @return boolean
     */
    String value() default "";

    /**
     * 设置此元素是否存在,设置否时不会去匹配
     * @return boolean
     */
    boolean isExists() default true;

    /**
     * 在输出是确定在哪一列
     */
    int index() default 0x10000;
}
