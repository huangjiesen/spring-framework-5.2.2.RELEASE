package com.ofwiki.mapper;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author HuangJS
 * @date 2019-08-29 2:22 PM
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MapperBeanDefinitionRegistryPostProcessor.class)
public @interface MapperScan {
    String[] value();
}
