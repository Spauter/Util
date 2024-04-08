package com.bloducspauter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扫描
 * @author Blodcu Spauter
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelTableScan {
    /**
     *  需要扫描的多个包
     */
    String[] basePackes() default "";

    /**
     * 需要扫描的单个包
     */
    String basePack()default "";
}
