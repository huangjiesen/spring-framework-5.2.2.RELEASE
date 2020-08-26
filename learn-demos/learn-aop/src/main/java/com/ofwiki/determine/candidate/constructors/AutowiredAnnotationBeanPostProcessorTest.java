package com.ofwiki.determine.candidate.constructors;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HuangJS
 * @date 2020-08-26 10:17 下午
 */
@ComponentScan("com.ofwiki.determine.candidate.constructors")
public class AutowiredAnnotationBeanPostProcessorTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AutowiredAnnotationBeanPostProcessorTest.class);

        UserService bean = applicationContext.getBean(UserService.class);
        System.out.println(bean);
    }
}
