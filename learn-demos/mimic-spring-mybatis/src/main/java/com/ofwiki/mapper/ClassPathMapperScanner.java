package com.ofwiki.mapper;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * @author HuangJS
 * @date 2019-08-29 3:21 PM
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        // 获取指定包下的 BeanDefinition
        Set<BeanDefinitionHolder> hodlers = super.doScan(basePackages);
        if (hodlers.isEmpty()) {
            logger.warn("No mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(hodlers);
        }
        return hodlers;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        System.out.println("start register mappers");
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();

            // 修改bean class为MapperFactoryBean，并将原bean作为构造参数传入
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName); // 构造函数参数
            definition.setBeanClass(MapperFactoryBean.class);                                 // factory bean
            definition.getPropertyValues().add("proxyTargetName", beanClassName); // 属性注入

            // 根据类型自动注入 - 对MapperFactoryBean的一些属性的set方法进行自动调用，以注入spring容器存在的引用
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

            System.out.println("register mapper " + beanClassName);
        }
        System.out.println("success register mappers\n");
    }

    // 必须使用注解 @Mapper
    public void registerFilters() {
        // include filter
        addIncludeFilter(new AnnotationTypeFilter(Mapper.class));

        // exclude filter
        //addExcludeFilter((metadataReader, metadataReaderFactory) -> false);
    }


    // 必须是接口
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
