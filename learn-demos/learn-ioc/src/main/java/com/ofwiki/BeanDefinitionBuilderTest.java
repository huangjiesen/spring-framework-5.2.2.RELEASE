package com.ofwiki;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * BeanDefinitionBuilder使用示例
 * @author HuangJS
 * @date 2020-02-15 5:30 下午
 */

@Import(BeanDefinitionBuilderTest.MyBeanDefinitionRegistryPostProcessor.class)
public class BeanDefinitionBuilderTest {
    public static void main(String[] args) {
        ApplicationContext application = new AnnotationConfigApplicationContext(BeanDefinitionBuilderTest.class);
        UserService userService = application.getBean(UserService.class);
        Assert.notNull(userService, "userService is null");
    }

    // 通过@Import注册ImportBeanDefinitionRegistrar的实现类
    public static class MyBeanDefinitionRegistryPostProcessor implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            // 通过BeanDefinitionBuilder构建BeanDefinition
            BeanDefinition definition = BeanDefinitionBuilder
                    .genericBeanDefinition(UserService.class)
                    .setInitMethodName("sayHi")
                    //.setScope(BeanDefinition.SCOPE_PROTOTYPE)
                    //.addPropertyReference("dao","userDao")、
                    .getBeanDefinition();

            // 注册BeanDefinition
            registry.registerBeanDefinition("user",definition);
        }
    }

    public static class UserService {
        public void sayHi() {
            System.out.println("this is init method for UserService.");
        }
    }
}
