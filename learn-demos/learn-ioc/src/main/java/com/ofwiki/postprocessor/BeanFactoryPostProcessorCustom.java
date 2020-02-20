package com.ofwiki.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author HuangJS
 * @date 2020-02-20 12:21 下午
 */
@Component
public class BeanFactoryPostProcessorCustom implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        System.out.println("bean class name:"+beanDefinition.getBeanClassName());
        System.out.println("当前BeanFactory中有"+beanFactory.getBeanDefinitionCount()+" 个Bean");
        System.out.println("bean names:" + Arrays.asList(beanFactory.getBeanDefinitionNames()));
    }
}
