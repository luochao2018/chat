package com.web.chat.config;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    /**
     * 切换数据源名称
     * 默认 MASTER
     *
     * @return
     */
    DataSourceType value() default DataSourceType.MASTER;
}