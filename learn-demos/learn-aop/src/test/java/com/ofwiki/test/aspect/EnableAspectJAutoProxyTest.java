package com.ofwiki.test.aspect;

import com.ofwiki.test.aspect.service.UserService;
import org.aspectj.weaver.Advice;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.Assert;

/**
 * @author HuangJS
 * @date 2020-08-25 10:08 上午
 */
@EnableAspectJAutoProxy
@ComponentScan("com.ofwiki.test.aspect")
public class EnableAspectJAutoProxyTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(EnableAspectJAutoProxyTest.class);

        ServicesAspect aspect = applicationContext.getBean(ServicesAspect.class);

        Assert.notNull(aspect,"aspect is null");
        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService);
        userService.save();

    }
}
