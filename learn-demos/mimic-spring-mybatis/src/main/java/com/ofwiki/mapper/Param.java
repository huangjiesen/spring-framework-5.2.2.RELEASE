package com.ofwiki.mapper;

import java.lang.annotation.*;

/**
 * @author HuangJS
 * @date 2019-08-29 11:12 AM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    String value();
}
