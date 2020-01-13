package com.ofwiki.mapper;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author HuangJS
 * @date 2019-08-29 1:48 PM
 */
public class MapperBeanDefinitionRegistryPostProcessor implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取MapperScan注解实例
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }

        // 获取要扫描的包名
        String[] values = annoAttrs.getStringArray("value");
        if (values == null) {
            throw new RuntimeException("No mapper package found,Please check your configuration");
        }

        // 设置bean扫描过虑
        scanner.registerFilters();

        // 执行扫描注册
        scanner.doScan(values);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
