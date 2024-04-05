package com.bloducspauter.annotation;

/**
 * @author Bloduc Spauter
 */
public @interface CellName {
    /**
     * 表格元素
     * @return boolean
     */
    String cellName() default "";

    /**
     * 设置此元素是否存在,设置否时不会去匹配
     * @return boolean
     */
    boolean isExsists() default true;
}
